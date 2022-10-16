(ns dunnage.proto.deserializer
  (:require [dunnage.proto.serde :as serde]
            [malli.core :as m]
            [clojure.java.io :as io])
  (:import (com.google.protobuf CodedInputStream
                                CodedOutputStream
                                WireFormat
                                UnknownFieldSet
                                ExtensionRegistry
                                ByteString)))
(declare make-deserializer)

(defn schema->wire-type [schema packed]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0) packed)
    :malli.core/schema (recur (nth (m/children schema) 0) packed )
    :ref (recur (m/deref schema) packed)
    :map 2
    :sequential 2
    :enum  0
    string? 2
    double? 1
    float? 5
    int? 0
    pos-int? 0
    bytes? 2
    boolean? 0))

(defn x [primitive]
  (case primitive
    "TYPE_DOUBLE" 1
    "TYPE_FLOAT" 5
    "TYPE_INT64" 0
    "TYPE_UINT64" 0
    "TYPE_INT32" 0
    "TYPE_FIXED64" 1
    "TYPE_FIXED32"5
    "TYPE_BOOL" 0
    "TYPE_STRING" 2
    ;"TYPE_GROUP"
    "TYPE_MESSAGE" 2
    "TYPE_BYTES" 2
    "TYPE_UINT32" 0
    "TYPE_ENUM" 0
    "TYPE_SFIXED32" 5
    "TYPE_SFIXED64" 1
    "TYPE_SINT32" 0
    "TYPE_SINT64" 0))

(defn primitive-parser [primitive]
  (case primitive
    "TYPE_DOUBLE" serde/cis->Double
    "TYPE_FLOAT" serde/cis->Float
    "TYPE_INT64" serde/cis->Int64
    "TYPE_UINT64" serde/cis->UInt64
    "TYPE_INT32" serde/cis->Int32
    "TYPE_FIXED64" serde/cis->Fixed64
    "TYPE_FIXED32" serde/cis->Fixed32
    "TYPE_BOOL" serde/cis->Bool
    "TYPE_STRING" serde/cis->String
    ;"TYPE_GROUP" serde/cis->GROUP
    ;"TYPE_MESSAGE" serde/cis->MESSAGE
    "TYPE_BYTES" serde/cis->Bytes
    "TYPE_UINT32" serde/cis->UInt32
    "TYPE_ENUM" serde/cis->Enum
    "TYPE_SFIXED32" serde/cis->SFixed32
    "TYPE_SFIXED64" serde/cis->SFixed64
    "TYPE_SINT32" serde/cis->SInt32
    "TYPE_SINT64" serde/cis->SInt64))

(defn sequence? [schema]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0))
    :malli.core/schema (recur (nth (m/children schema) 0))
    :ref (recur (m/deref schema))
    :sequential true
    false))

(defn primitive? [schema]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0))
    :malli.core/schema (recur (nth (m/children schema) 0))
    :ref (recur (m/deref schema))
    :sequential false
    :map false
    true))

(defn packable? [schema]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0))
    :malli.core/schema (recur (nth (m/children schema) 0))
    :ref (recur (m/deref schema))
    :sequential (primitive? schema)
    false))

(defn submessage? [schema]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0))
    :malli.core/schema (recur (nth (m/children schema) 0))
    :ref (recur (m/deref schema))
    :map true
    false))

(defn enum-deserializer [schema]
  (let [properties (m/properties  schema)
        schema-values (m/children schema)
        values  (into {} (map
                           (juxt
                             (comp :protobuf/fieldnumber #(nth % 1))
                             #(nth % 0)))
                      schema-values)]
    (fn [^CodedInputStream is]
      (values (serde/cis->Enum is))))
  )
(defn map-deserializer [schema referenced-deserializer]
  (let [properties (m/properties  schema)
        entries (m/children schema)
        sub-parser (reduce
                     (fn [acc[k {:keys [protobuf/fieldnumber primitive]} subtype :as entry]]
                       (let [wire-type (schema->wire-type subtype false)
                             wire-type-packed (schema->wire-type subtype true)
                             deser (case primitive
                                     "TYPE_MESSAGE" (make-deserializer subtype referenced-deserializer)
                                     "TYPE_ENUM" (make-deserializer subtype referenced-deserializer)
                                     (primitive-parser primitive))]
                         (if (sequence? subtype)
                           (case primitive
                             "TYPE_MESSAGE"
                             (assoc acc (serde/make-tag fieldnumber wire-type)
                                        (fn [acc ^CodedInputStream is]
                                          (update acc k conj (serde/cis->embedded deser is))))
                             ("TYPE_STRING" "TYPE_BYTES")
                             (assoc acc (serde/make-tag fieldnumber wire-type)
                                        (fn [acc ^CodedInputStream is]
                                          (update acc k conj (deser is))))
                             (assoc acc (serde/make-tag fieldnumber wire-type)
                                        (fn [acc ^CodedInputStream is]
                                          (update acc k (serde/cis->repeated deser is)))
                                        (serde/make-tag fieldnumber wire-type-packed)
                                        (fn [acc ^CodedInputStream is]
                                          (update acc k (serde/cis->packedrepeated deser is)))))
                           (case primitive
                             "TYPE_MESSAGE"
                             (assoc acc (serde/make-tag fieldnumber wire-type)
                                        (fn [acc ^CodedInputStream is]
                                          (assoc acc k (serde/cis->embedded deser is))))
                             (assoc acc (serde/make-tag fieldnumber wire-type)
                                        (fn [acc ^CodedInputStream is]
                                          (assoc acc k (deser is))))))))
                     {}
                         entries)]
    (fn [^CodedInputStream is]
      (loop [acc {} tag (.readTag ^CodedInputStream is)]
        (if (pos? tag)
          (let [sub (sub-parser tag)]
            ; (prn tag acc)
            (assert sub (pr-str tag (keys sub-parser) entries))
            (recur (sub acc is)
                   (.readTag ^CodedInputStream is)))
          acc)))))

(defn seq-deserializer [schema referenced-deserializer]
  (let [properties (m/properties  schema)
        subschema (nth (m/children schema) 0)
        subdeser  (make-deserializer subschema referenced-deserializer)]
    subdeser))

(defn make-deserializer [schema referenced-deserializer]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0) referenced-deserializer)
    :malli.core/schema (recur (nth (m/children schema) 0) referenced-deserializer)
    :ref (let [r (nth (m/children schema) 0)]
           (if-some [deser (get @referenced-deserializer r)]
             deser
             (let [deser (fn [cis]
                           (let [deser (get @referenced-deserializer r)]
                             (deser cis)))]
               ;add stub
               (swap! referenced-deserializer assoc r deser)
               (swap! referenced-deserializer assoc r (make-deserializer (m/deref schema) referenced-deserializer))
               (get @referenced-deserializer r))))
    :map (map-deserializer schema referenced-deserializer)
    :sequential (seq-deserializer schema referenced-deserializer)
    :enum  (enum-deserializer schema)
    string? serde/cis->String
    double? serde/cis->Double
    int? serde/cis->Int32
    bytes? serde/cis->Bytes
    boolean? serde/cis->Bool))

(comment
  (require 'dunnage.proto.schema)
  (def deser (make-deserializer dunnage.proto.schema/descriptor (atom {})))

  (deser (CodedInputStream/newInstance (io/input-stream (io/resource "descriptor.descriptorset"))))
  (deser (CodedInputStream/newInstance (io/input-stream (io/resource "descriptor.descriptorset.out"))))
  (deser (CodedInputStream/newInstance (io/input-stream (io/resource "handshaker.descriptorset"))))
  (deser (CodedInputStream/newInstance (io/input-stream (io/resource "transport_security_common.descriptorset"))))



  )