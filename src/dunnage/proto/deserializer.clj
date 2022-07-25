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

(defn map-deserializer [schema referenced-deserializer]
  (let [properties (m/properties  schema)
        entries (m/children schema)
        key-fn  (into {} (map
                           (juxt
                             (comp :protobuf/fieldnumber #(nth % 1))
                             #(nth % 0)))
                      entries)
        sub-parser (into {} (map
                              (juxt
                                (comp :protobuf/fieldnumber #(nth % 1))
                                #(let [subtype (nth % 2)
                                       deser (make-deserializer subtype referenced-deserializer)]
                                   (if (submessage? subtype)
                                     (fn [^CodedInputStream is] (serde/cis->embedded deser is))
                                     deser))))
                         entries)
        sub-seq  (into {} (map
                              (juxt
                                (comp :protobuf/fieldnumber #(nth % 1))
                                #(sequence? (nth % 2))))
                         entries)
        subfn  (fn [^CodedInputStream is]
                 (loop [acc {} tag (.readTag ^CodedInputStream is)]
                   (if (pos? tag)
                     (let [idx (bit-shift-right tag 3)
                           wire-type (WireFormat/getTagWireType tag)
                           fieldnumber (WireFormat/getTagFieldNumber tag)
                           type (bit-and 0x2 tag)
                           k (key-fn idx)
                           sub (sub-parser idx)
                           sub-seq? (sub-seq idx)]
                       (prn k type)
                       (assert sub (pr-str idx wire-type fieldnumber tag   (keys sub-parser) type entries))
                       (recur (if sub-seq?
                                (update acc k (case type
                                                0 (serde/cis->repeated sub is)
                                                2 (serde/cis->packedrepeated sub is)
                                                ;(sub is)
                                                ))
                                (assoc acc k (sub is)))
                              (.readTag ^CodedInputStream is)))
                     acc)))]
    #_(serde/cis->embedded subfn %)
    subfn))

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
               deser)))
    :map (map-deserializer schema referenced-deserializer)
    :sequential (seq-deserializer schema referenced-deserializer)
    :enum  serde/cis->Enum
    string? serde/cis->String
    double? serde/cis->Double
    int? serde/cis->Int32
    bytes? serde/cis->Bytes
    boolean? serde/cis->Bool
    )
  )

(comment
  (require 'dunnage.proto.schema)
  (let [deser (make-deserializer dunnage.proto.schema/descriptor (atom {}))
        is (CodedInputStream/newInstance (io/input-stream (io/resource "descriptor.descriptorset")))]
    (deser is))

  )