(ns dunnage.proto.bootstrap
  (:require [clojure.java.io :as io])
  (:import com.google.protobuf.DescriptorProtos
           (com.google.protobuf DescriptorProtos$FileDescriptorSet DescriptorProtos$FileDescriptorSet DescriptorProtos$DescriptorProto DescriptorProtos$FieldDescriptorProto))
  )

(defn handle-type [t]
  (case  t
    ; 0 is reserved for errors.
    ; Order is weird for historical reasons.
        1 double?; TYPE_DOUBLE
        2 float?; TYPE_FLOAT
    ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT64 if
    ; negative values are likely.
        3 int?; TYPE_INT64
        4 int?; TYPE_UINT64
    ; Not ZigZag encoded.  Negative numbers take 10 bytes.  Use TYPE_SINT32 if
    ; negative values are likely.
        5 int?; TYPE_INT32
        6 int?; TYPE_FIXED64
        7 int?; TYPE_FIXED32
        8 boolean?; TYPE_BOOL
        9 string?; TYPE_STRING
    ; Tag-delimited aggregate.
    ; Group type is deprecated and not supported in proto3. However, Proto3
    ; implementations should still be able to parse the group wire format and
    ; treat group fields as unknown fields.
        10 nil; TYPE_GROUP
        11 nil; TYPE_MESSAGE   ; // Length-delimited aggregate.

    ; New in version 2.
        12 bytes?; TYPE_BYTES
        13 int?; TYPE_UINT32
        14 keyword; TYPE_ENUM
        15 int?; TYPE_SFIXED32
        16 int?; TYPE_SFIXED64
        17 int?; TYPE_SINT32   ; // Uses ZigZag encoding.
        18 int?; TYPE_SINT64   ; // Uses ZigZag encoding.
    ))

(defn field->kv [^DescriptorProtos$FieldDescriptorProto field]
  [(keyword (.getName field))
   {:protobuf/fieldnumber (.getNumber field)}
   (handle-type (.getType field) )])

(defn dproto->malli [^DescriptorProtos$DescriptorProto dproto]
  (into [:map {}]
        (map field->kv)
        (.getFieldList dproto)))

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
  )