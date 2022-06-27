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

(defn map-deserializer [schema]
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
                                (comp make-deserializer #(nth % 2))))
                         entries)
        subfn  (fn [^CodedInputStream is]
                 (loop [acc {} tag (.readTag ^CodedInputStream is)]
                   (if (pos? tag)
                     (let [idx (bit-shift-right tag 3)
                           wire-type (WireFormat/getTagWireType tag)
                           k (key-fn idx)
                           sub (sub-parser idx)]
                       (recur (update acc k (sub wire-type is))
                              (.readTag ^CodedInputStream is)))
                     acc)))]
    #(serde/cis->embedded subfn %)))

(defn seq-deserializer [schema]
  (let [properties (m/properties  schema)
        subschema (nth (m/children schema) 0)
        subdeser  (make-deserializer subschema)]
    #(serde/cis->packablerepeated subdeser %)))

(defn make-deserializer [schema ]
  (case (m/type schema)
    :schema (recur (nth (m/children schema) 0))
    :malli.core/schema (recur (nth (m/children schema) 0))
    :map (map-deserializer schema)
    :sequential (seq-deserializer schema))
  )

(comment
  (require 'dunnage.proto.schema)
  (let [deser (make-deserializer dunnage.proto.schema/descriptor)
        is (CodedInputStream/newInstance (io/input-stream (io/resource "descriptor.descriptorset")))]
    (deser is))

  )