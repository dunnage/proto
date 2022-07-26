(ns dunnage.proto.proto-schema
  (:require [clojure.java.io :as io]
            [clojure.string])
  (:import (com.google.protobuf CodedInputStream)))

(set! *print-namespace-maps* false)

(defn parse-type-name [tn]
  (let [parts (clojure.string/split tn #"\.")]
    (keyword
      (clojure.string/join "." (butlast (rest parts)))
      (last parts))))

(defn handle-type [{:keys [type type_name ] :as field}]
  (case type
    ; 0 is reserved for errors.
    ; Order is weird for historical reasons.
    "TYPE_DOUBLE" 'double?
    "TYPE_FLOAT" 'float?
    ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT64 if
    ; negative values are likely.
    "TYPE_INT64" 'int?
    "TYPE_UINT64" 'pos-int?
    ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT32 if
    ; negative values are likely.
    "TYPE_INT32" 'int?
    "TYPE_FIXED64" 'int?
    "TYPE_FIXED32" 'int?
    "TYPE_BOOL" 'boolean?
    "TYPE_STRING" 'string?
    ; Tag-delimited aggregate.
    ; Group type is deprecated and not supported in proto3. However, Proto3
    ; implementations should still be able to parse the group wire format and
    ; treat group fields as unknown fields.
    "TYPE_GROUP" (assert false "does not parse proto groups")
    "TYPE_MESSAGE" [:ref (parse-type-name type_name)]       ; // Length-delimited aggregate.

    ; New in version 2.
    "TYPE_BYTES" 'bytes?
    "TYPE_UINT32" 'pos-int?
    "TYPE_ENUM" [:ref (parse-type-name type_name)]
    "TYPE_SFIXED32" 'int?
    "TYPE_SFIXED64" 'int?
    "TYPE_SINT32" 'int?                                     ; // Uses ZigZag encoding.
    "TYPE_SINT64" 'int?                                     ; // Uses ZigZag encoding.
    ))

;TYPE_DOUBLE
;TYPE_FLOAT
;TYPE_INT64
;TYPE_UINT64
;TYPE_INT32
;TYPE_FIXED64
;TYPE_FIXED32
;TYPE_BOOL
;TYPE_STRING
;TYPE_GROUP
;TYPE_MESSAGE
;TYPE_BYTES
;TYPE_UINT32
;TYPE_ENUM
;TYPE_SFIXED32
;TYPE_SFIXED64
;TYPE_SINT32
;TYPE_SINT64
;LABEL_OPTIONAL
;LABEL_REQUIRED
;LABEL_REPEATED


(defn field->kv [{:keys [name
                         number
                         label
                         type
                         type_name
                         extendee
                         default_value
                         oneof_index
                         json_name
                         options
                         proto3_optional] :as field}]
  (let [t (handle-type field)]
    [(keyword name)
     (cond-> {:protobuf/fieldnumber number}
             (not= label "LABEL_REQUIRED") (assoc :optional true)
             type (assoc :primitive type))
     (if (= label "LABEL_REPEATED")
       [:sequential t]
       t)]))

(defn descriptor-enum->malli [registry [package path {:keys [name
                                                             value
                                                             options
                                                             reserved_range
                                                             reserved_name]
                                                       :as dproto}]]
  (assoc registry
    (keyword package name)
    (into [:enum]
          (map (fn [{:keys [name number]}]
                 [name {:protobuf/fieldnumber number}]))
          value)))

(defn descriptor-proto->malli [registry [package path {:keys [name
                                                              field
                                                              extension
                                                              nested_type
                                                              enum_type
                                                              extension_range
                                                              oneof_decl
                                                              options
                                                              reserved_range
                                                              reserved_name]
                                                       :as dproto}]]
  (let [data [(str package "." name) []]]
    (as-> (assoc registry
            (keyword package name)
            (into [:map {}]
                  (map field->kv)
                  field))
          registry
          (transduce (map-indexed (fn [idx subtype]
                                    (-> data
                                        (update 1 conj 4 idx)
                                        (conj subtype))))
                     (completing descriptor-proto->malli)
                     registry
                     nested_type)
          (transduce (map-indexed (fn [idx subtype]
                                    (-> data
                                        (update 1 conj 5 idx)
                                        (conj subtype))))
                     (completing descriptor-enum->malli)
                     registry enum_type))))

(defn file-descriptor->malli [registry {:keys [name package dependency
                                               public_dependency weak_dependency
                                               message_type enum_type service
                                               extension options source_code_info
                                               syntax]
                                        :as dproto}]
  (let [data [package []]]
    (as-> registry registry
          (transduce (map-indexed (fn [idx subtype]
                                    (-> data
                                        (update 1 conj 4 idx)
                                        (conj subtype))))
                     (completing descriptor-proto->malli)
                     registry
                     message_type)
          (transduce (map-indexed (fn [idx subtype]
                                    (-> data
                                        (update 1 conj 5 idx)
                                        (conj subtype))))
                     (completing descriptor-enum->malli)
                     registry
                     enum_type)))

  )

(comment
  (require 'dunnage.proto.schema)
  (require 'dunnage.proto.deserializer)
  (def fdescset (let [deser (dunnage.proto.deserializer/make-deserializer dunnage.proto.schema/descriptor (atom {}))
                      is (CodedInputStream/newInstance (io/input-stream (io/resource "descriptor.descriptorset")))]
                  (deser is)))
  (reduce file-descriptor->malli {} (:file fdescset))

  )