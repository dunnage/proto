(ns dunnage.proto.bootstrap
  (:require [clojure.java.io :as io]
            [clojure.string])
  (:import (com.google.protobuf
             DescriptorProtos$FileDescriptorSet
             DescriptorProtos$FileDescriptorSet
             DescriptorProtos$DescriptorProto
             DescriptorProtos$FieldDescriptorProto
             ProtocolMessageEnum DescriptorProtos$FileDescriptorProto DescriptorProtos$EnumDescriptorProtoOrBuilder DescriptorProtos$EnumDescriptorProto DescriptorProtos$EnumValueDescriptorProto DescriptorProtos$MessageOptionsOrBuilder)))

(set! *print-namespace-maps* false)

(defn parse-type-name [tn]
  (let [parts (clojure.string/split tn #"\.")]
    (keyword
      (clojure.string/join "." (butlast (rest parts)))
      (last parts))))

(defn handle-type [^DescriptorProtos$FieldDescriptorProto field]
  (let [^ProtocolMessageEnum t (.getType field)]
    ;(prn (.getOptions field))
    (case (.getNumber t)
      ; 0 is reserved for errors.
      ; Order is weird for historical reasons.
      1 'double?                                            ; TYPE_DOUBLE
      2 'float?                                             ; TYPE_FLOAT
      ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT64 if
      ; negative values are likely.
      3 [:schema {:primitive "INT64"} 'int?]                                               ; TYPE_INT64
      4 [:schema {:primitive "UINT64"} 'int?]                                               ; TYPE_UINT64
      ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT32 if
      ; negative values are likely.
      5 [:schema {:primitive "INT32"} 'int?]                                               ; TYPE_INT32
      6 [:schema {:primitive "FIXED64"} 'int?]                                               ; TYPE_FIXED64
      7 [:schema {:primitive "FIXED32"} 'int?]                                               ; TYPE_FIXED32
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
      13 [:schema {:primitive "UINT32"} 'int?]                                              ; TYPE_UINT32
      14 [:ref (parse-type-name (.getTypeName field))]                                 ; TYPE_ENUM
      15 [:schema {:primitive "SFIXED32"} 'int?]                                              ; TYPE_SFIXED32
      16 [:schema {:primitive "SFIXED64"} 'int?]                                              ; TYPE_SFIXED64
      17 [:schema {:primitive "SINT32"} 'int?]                                              ; TYPE_SINT32   ; // Uses ZigZag encoding.
      18 [:schema {:primitive "SINT64"} 'int?]                                              ; TYPE_SINT64   ; // Uses ZigZag encoding.
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

(defn dproto->malli [registry protos]
  (if (empty? protos)
    registry
    (let [[package dproto] (first protos)
          subtypes (cond
                     (instance? DescriptorProtos$DescriptorProto dproto)
                         (do (assert (empty? (.getExtensionList dproto)))
                             ;(assert (empty? (.getWeakDependencyList dproto)))
                             ;(assert (empty? (.getPublicDependencyList dproto)))
                             ;(prn (.getOptions dproto))
                             (-> []
                                 (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getNestedTypeList dproto))
                                 (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getEnumTypeList dproto))
                                 (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getOneofDeclList dproto))
                                 ; (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getOptions dproto))

                                 ))
                     (instance? DescriptorProtos$MessageOptionsOrBuilder dproto)
                     (do (assert (empty? (.getExtensionList dproto)))
                         ;(assert (empty? (.getWeakDependencyList dproto)))
                         ;(assert (empty? (.getPublicDependencyList dproto)))
                         (-> []
                             (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getNestedTypeList dproto))
                             (into (map (fn [x] [(str package "." (.getName dproto)) x])) (.getEnumTypeList dproto))))

                     :default [])]
      (recur
        (assoc registry
          (keyword package (.getName dproto))
          (cond (or (instance? DescriptorProtos$DescriptorProto dproto)
                    (instance? DescriptorProtos$FileDescriptorProto dproto))
                (into [:map {}]
                      (map field->kv)
                      (.getFieldList dproto))
                (instance? DescriptorProtos$EnumDescriptorProto dproto)
                (do
                  ;(prn (.getValueList dproto) #_(bean dproto))
                  (into [:enum]
                        (map (fn [^DescriptorProtos$EnumValueDescriptorProto val]
                               [(.getName val) {:protobuf/fieldnumber (.getNumber val)}]))
                        (.getValueList dproto)))
                ; :default [:any]
                ))
        (cond-> (rest protos)
                (not-empty subtypes)
                (into subtypes))))))


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
  (dproto->malli  {} (into [] (map (fn [x] [(.getPackage fdesc) x])) (.getMessageTypeList fdesc)))
  (transduce
    (comp (map (fn [filelist]
                 (mapv (fn [x] [(.getPackage filelist) x] ) (.getMessageTypeList filelist))
                 )))
    (completing dproto->malli)
    {}
    (.getFileList fdescset))

  (into []
        (comp (map (fn [filelist]
                     (mapv (fn [x] [(.getPackage filelist) x] ) (.getMessageTypeList filelist))
                     )))
        (.getFileList fdescset))

  )