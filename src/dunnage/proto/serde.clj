(ns dunnage.proto.serde
  ; (:require)
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pprint])
  (:import (com.google.protobuf CodedInputStream
                                CodedOutputStream
                                WireFormat
                                UnknownFieldSet
                                ExtensionRegistry
                                ByteString)
           (clojure.lang IReduceInit)
           (java.io ByteArrayOutputStream)))

(set! *warn-on-reflection* true)

(def default-scalar? #(or (nil? %) (zero? %)))
(def default-bytes? empty?)
(def default-bool? #(not (true? %)))

(defn make-tag [^long field ^long  wire-type]
  (bit-or (bit-shift-left field 3) wire-type))

(defmacro defparsefn [type]
  (let [name (symbol (str "cis->" type))
        sym (symbol (str "read" type))
        doc (format "Deserialize a '%s' type" type)]
    `(defn ~name ~doc [^CodedInputStream is#]
       (. is# ~sym))))

(defmacro defwritefn [type default?]
  (let [name (symbol (str "write-" type))
        optimized-name (symbol (str "write-optimized-" type))
        sym (symbol (str "write" type))
        doc (format "Serialize a '%s' type" type)]
    `(do
      (defn ~name ~doc
         [tag# value# ^CodedOutputStream os#]
         (. os# ~sym tag# value#))
      (defn ~optimized-name ~doc
         [tag# value# ^CodedOutputStream os#]
         (when-not (~default? value#)
           (. os# ~sym tag# value#))))))

(defmacro defserdes [type default?]
  `(do
     (defparsefn ~type)
     (defwritefn ~type ~default?)))

(defmacro defscalar [type]
  `(defserdes ~type `default-scalar?))

(defscalar "Double")
(defscalar "Enum")
(defscalar "Fixed32")
(defscalar "Fixed64")
(defscalar "Float")
(defscalar "Int32")
(defscalar "Int64")
(defscalar "SFixed32")
(defscalar "SFixed64")
(defscalar "SInt32")
(defscalar "SInt64")
(defscalar "UInt32")
(defscalar "UInt64")

(defserdes "String" default-bytes?)
(defserdes "Bool" default-bool?)

;; manually implement the "Bytes" scalar so we can properly handle native byte-array import/export
(defn cis->Bytes
  "Deserialize 'Bytes' type"
  [^CodedInputStream is]
  (.readByteArray is))

(defn write-Bytes
  "Serialize 'Bytes' type"
  [tag value ^CodedOutputStream os]
  (let [bytestring (ByteString/copyFrom (bytes value))]
    (.writeBytes os tag bytestring)))

(defn write-optimized-Bytes
  "Serialize 'Bytes' type"
  [tag value ^CodedOutputStream os]
  (when-not (empty? value)
    (let [bytestring (ByteString/copyFrom (bytes value))]
      (.writeBytes os tag bytestring))))

(defn cis->undefined
  "Deserialize an unknown type, retaining its tag/type"
  [tag ^CodedInputStream is]
  (let [num (WireFormat/getTagFieldNumber tag)
        type (WireFormat/getTagWireType tag)]
    (case type
      0 (.readInt64 is)
      1 (.readFixed64 is)
      2 (.readBytes is)
      3 (.readGroup is num (UnknownFieldSet/newBuilder) (ExtensionRegistry/getEmptyRegistry))
      4 nil
      5 (.readFixed32 is))))

(defn cis->embedded
  "Deserialize an embedded type, where **f** is an (fn) that can deserialize the embedded message"
  [f ^CodedInputStream is]
  (let [len (.readRawVarint32 is)
        lim (.pushLimit is len)]
    ;(prn :cis->embedded  len lim)
    (let [result (f is)]
      (when-not (zero? (.getBytesUntilLimit is))
        ;(prn :getBytesUntilLimit (.getBytesUntilLimit is))
        (.readRawBytes is (.getBytesUntilLimit is)))

      (.popLimit is lim)
      result)))

(defn pb-bytes [serializer item]
  (let [bos (ByteArrayOutputStream.)
        os ^CodedOutputStream  (CodedOutputStream/newInstance bos)]
    (serializer item os)
    (.toByteArray bos)))

(defn write-embedded
  "Serialize an embedded type along with tag/length metadata"
  [tag serializer item ^CodedOutputStream os]
  (when (some? item)
    (let [data (pb-bytes serializer item)
          len (count data)]
      (.writeTag os tag 2);; embedded messages are always type=2 (string)
      (.writeUInt32NoTag os len)
      (.writeRawBytes os (bytes data)))))



(defn cis->map
  "Deserialize a wire format map-type to user format [key val]"
  [f is]
  (let [{:keys [key value]} (f is)]
    (partial into {key value})))

(defn cis->repeated
  "Deserialize an 'unpacked' repeated type (see [[cis->packablerepeated]])"
  [f is]
  (fn [coll]
    (conj (or coll []) (f is))))

(defn repeated-reducible [fnext ^CodedInputStream is]
  (reify IReduceInit
    (reduce [_ f init]
      (loop [acc init]
        #_(prn :bul (.getBytesUntilLimit is))
        (if (and (not (.isAtEnd is))
                 (not (reduced? acc)))
          (let [x (f acc (fnext is))]
            (recur x))
          (unreduced acc))))))



(defn cis->packedrepeated
  "Deserialize a 'packed' repeated type (see [[cis->packablerepeated]])"
  [f is]
  (fn [coll]
    (cis->embedded #(into (or coll []) (repeated-reducible f %)) is)))

;; FIXME: Add support for optimizing packable types
(defn write-repeated
  "Serialize a repeated type"
  [f tag items os]
  (doseq [item items]
    (f tag item os)))

(defn write-map
  "Serialize user format [key val] using given map item constructor"
  [constructor tag items os]
  (write-repeated write-embedded tag (map (fn [[key value]] (constructor {:key key :value value})) items) os))
