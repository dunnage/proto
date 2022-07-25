(ns dunnage.proto.bootstrap
  (:require [clojure.java.io :as io]
            [clojure.string])
  (:import (com.google.protobuf
             DescriptorProtos$FileDescriptorSet
             DescriptorProtos$FileDescriptorSet
             DescriptorProtos$DescriptorProto
             DescriptorProtos$FieldDescriptorProto
             ProtocolMessageEnum DescriptorProtos$FileDescriptorProto DescriptorProtos$EnumDescriptorProtoOrBuilder DescriptorProtos$EnumDescriptorProto DescriptorProtos$EnumValueDescriptorProto)))

(set! *print-namespace-maps* false)

(defn parse-type-name [tn]
  (let [parts (clojure.string/split tn #"\.")]
    (keyword
      (clojure.string/join "." (butlast (rest parts)))
      (last parts))))

(defn parse-enum [^DescriptorProtos$FieldDescriptorProto field]
  ;(let [enum (.getDescriptorForType field)])
  (prn (bean (.getDescriptorForType field)))
  [:enum]
  )

(defn handle-type [^DescriptorProtos$FieldDescriptorProto field]
  (let [^ProtocolMessageEnum t (.getType field)]
    (case (.getNumber t)
      ; 0 is reserved for errors.
      ; Order is weird for historical reasons.
      1 'double?                                            ; TYPE_DOUBLE
      2 'float?                                             ; TYPE_FLOAT
      ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT64 if
      ; negative values are likely.
      3 'int?                                               ; TYPE_INT64
      4 'int?                                               ; TYPE_UINT64
      ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT32 if
      ; negative values are likely.
      5 'int?                                               ; TYPE_INT32
      6 'int?                                               ; TYPE_FIXED64
      7 'int?                                               ; TYPE_FIXED32
      8 'boolean?                                           ; TYPE_BOOL
      9 'string?                                            ; TYPE_STRING
      ; Tag-delimited aggregate.
      ; Group type is deprecated and not supported in proto3. However, Proto3
      ; implementations should still be able to parse the group wire format and
      ; treat group fields as unknown fields.
      10 (assert false "does not parse proto groups")                                                ; TYPE_GROUP
      11 [:ref (parse-type-name (.getTypeName field))]                                                ; TYPE_MESSAGE   ; // Length-delimited aggregate.

      ; New in version 2.
      12 'bytes?                                            ; TYPE_BYTES
      13 'int?                                              ; TYPE_UINT32
      14 [:ref (parse-type-name (.getTypeName field))]                                 ; TYPE_ENUM
      15 'int?                                              ; TYPE_SFIXED32
      16 'int?                                              ; TYPE_SFIXED64
      17 'int?                                              ; TYPE_SINT32   ; // Uses ZigZag encoding.
      18 'int?                                              ; TYPE_SINT64   ; // Uses ZigZag encoding.
      )))

(defn field->kv [^DescriptorProtos$FieldDescriptorProto field]
  (let [label (.getNumber (.getLabel field))
        dtorname (.getName field)
        t (handle-type field)]
    [(keyword dtorname)
     (cond-> {:protobuf/fieldnumber (.getNumber field)}
             (not= label 2) (assoc :optional true))
     (if (= label 3)
       [:sequential t]
       t)]))

(defn dproto->malli [protos registry]
  (if (empty? protos)
    registry
    (let [[package dproto] (first protos)
          subtypes (cond (or (instance? DescriptorProtos$DescriptorProto dproto)
                             (instance? DescriptorProtos$FileDescriptorProto dproto))
                         (-> []
                             (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getNestedTypeList dproto))
                             (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getEnumTypeList dproto)))
                         :default [])]
      (recur
        (cond-> (rest protos)
                (not-empty subtypes)
                (into subtypes))
        (assoc registry
          (keyword package (.getName dproto))
          (cond (or (instance? DescriptorProtos$DescriptorProto dproto)
                    (instance? DescriptorProtos$FileDescriptorProto dproto))
                (into [:map {}]
                      (map field->kv)
                      (.getFieldList dproto))
                (instance? DescriptorProtos$EnumDescriptorProto dproto)
                (do
                  (prn (.getValueList dproto) #_(bean dproto))
                  (into [:enum]
                        (map (fn [^DescriptorProtos$EnumValueDescriptorProto val]
                               [(.getName val) {:number (.getNumber val)}]))
                        (.getValueList dproto)))
                :default [:any])
          )))))


(comment
  (def fdescset (DescriptorProtos$FileDescriptorSet/parseFrom (io/input-stream (io/resource "descriptor.descriptorset"))))
  (def fdesc (first (.getFileList fdescset)))
  (.getName fdesc)
  (.getPackage fdesc)
  (.getDependencyList fdesc)
  (.getEnumTypeList fdesc)
  (.getExtensionList fdesc)
  (.getMessageTypeList fdesc)
  (.getOptions fdesc)
  (def mtype (first (.getMessageTypeList fdesc)))
  (dproto->malli (into [] (map (fn [x] [(.getPackage fdesc) x])) (.getMessageTypeList fdesc)) {})
  (into {} (map (dproto->malli (.getPackage fdesc))) (.getMessageTypeList fdesc))
  )