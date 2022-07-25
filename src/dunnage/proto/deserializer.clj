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
                                #(make-deserializer (nth % 2) referenced-deserializer)))
                         entries)
        subfn  (fn [^CodedInputStream is]
                 (loop [acc {} tag (.readTag ^CodedInputStream is)]
                   (if (pos? tag)
                     (let [idx (bit-shift-right tag 3)
                           wire-type (WireFormat/getTagWireType tag)
                           type (bit-and 0x2 tag)
                           k (key-fn idx)
                           sub (sub-parser idx)]
                       (assert sub (pr-str idx tag ))
                       (recur (update acc k (sub is))
                              (.readTag ^CodedInputStream is)))
                     acc)))]
    #(serde/cis->embedded subfn %)))

(defn seq-deserializer [schema referenced-deserializer]
  (let [properties (m/properties  schema)
        subschema (nth (m/children schema) 0)
        subdeser  (make-deserializer subschema referenced-deserializer)]
    (if (:packed properties)
      #(serde/cis->packedrepeated subdeser %)
      #(serde/cis->repeated subdeser %))))

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