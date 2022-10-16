(ns dunnage.proto.serializer
  (:require [dunnage.proto.serde :as serde]
            [malli.core :as m]
            [clojure.java.io :as io])
  (:import (com.google.protobuf CodedInputStream
                                CodedOutputStream
                                WireFormat
                                UnknownFieldSet
                                ExtensionRegistry
                                ByteString)))
(declare make-serializer)
;0	Varint	int32, int64, uint32, uint64, sint32, sint64, bool, enum
;1	64-bit	fixed64, sfixed64, double
;2	Length-delimited	string, bytes, embedded messages, packed repeated fields
;3	Start group	groups (deprecated)
;4	End group	groups (deprecated)
;5	32-bit	fixed32, sfixed32, float
(defn schema->wire-type [schema packed]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0) packed)
    :malli.core/schema (recur (nth (m/children schema) 0) packed)
    :ref (recur (m/deref schema) packed)
    :map 2
    :sequential (if packed
                  2
                  (recur (nth (m/children schema) 0) packed))
    :enum 0
    string? 2
    double? 1
    float? 5
    int? 0
    pos-int? 0
    bytes? 2
    boolean? 0))

(defn sequence? [schema]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0))
    :malli.core/schema (recur (nth (m/children schema) 0))
    :ref (recur (m/deref schema))
    :sequential true
    false))

(defn submessage? [schema]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0))
    :malli.core/schema (recur (nth (m/children schema) 0))
    :ref (recur (m/deref schema))
    :map true
    false))

(defn enum-serializer [schema]
  (let [properties (m/properties schema)
        schema-values (m/children schema)
        values (into {} (map
                          (juxt
                            #(nth % 0)
                            (comp :protobuf/fieldnumber #(nth % 1))))
                     schema-values)]
    (fn [tag val ^CodedOutputStream os]
      (serde/write-Enum tag (values val) os)
      ))
  )



(defn map-serializer [schema referenced-serializer]
  (let [properties (m/properties schema)
        entries (m/children schema)
        sub-serializer (into {} (map
                                  (juxt
                                    #(nth % 0)
                                    #(let [subtype (nth % 2)
                                           fieldnumber ^int (:protobuf/fieldnumber (nth % 1))
                                           ser (make-serializer subtype referenced-serializer)
                                           tag (serde/make-tag fieldnumber (schema->wire-type subtype true))]
                                       (fn [val os]
                                         ;(prn tag subtype fieldnumber)
                                         (ser tag val os)))))
                             entries)
        ser-map (fn [val ^CodedOutputStream os]
                  (reduce-kv
                    (fn [_ k v]
                      (let [ser (sub-serializer k)]
                        (ser v os)))
                    nil
                    val))]
    (fn [tag val ^CodedOutputStream os]
      (serde/write-embedded tag ser-map val os)
      )))

(defn seq-serializer [schema referenced-serializer]
  (let [properties (m/properties schema)
        subschema (nth (m/children schema) 0)
        subdeser (make-serializer subschema referenced-serializer)]
    (fn [tag vals os]
      (serde/write-repeated subdeser tag vals os))))

;(defn tagged-write [schema ser]
;  (let [fieldnumber ^int (:protobuf/fieldnumber schema)
;        tag (serde/make-tag fieldnumber (schema->wire-type subtype true))]
;    (fn [val os]
;        (ser tag val os)))
;  )
(defn make-serializer [schema referenced-serializer]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0) referenced-serializer)
    :malli.core/schema (recur (nth (m/children schema) 0) referenced-serializer)
    :ref (let [r (nth (m/children schema) 0)]
           (if-some [ser (get @referenced-serializer r)]
             ser
             (let [ser (fn [tag value cos]
                         (let [deser (get @referenced-serializer r)]
                           (deser tag value cos)))]
               ;add stub
               (swap! referenced-serializer assoc r ser)
               (swap! referenced-serializer assoc r (make-serializer (m/deref schema) referenced-serializer))
               ser)))
    :map (map-serializer schema referenced-serializer)
    :sequential (seq-serializer schema referenced-serializer)
    :enum (enum-serializer schema)
    string? serde/write-String
    double? serde/write-Double
    int? serde/write-Int32
    pos-int? serde/write-Int32
    bytes? serde/write-Bytes
    boolean? serde/write-Bool
    )
  )

(comment
  (require 'dunnage.proto.schema)
  (let [ser (make-serializer dunnage.proto.schema/descriptor (atom {}))
        os (CodedOutputStream/newInstance (io/output-stream (io/file "dev-resources/descriptor.descriptorset.out")))]
    (ser 1 temp os))

  )