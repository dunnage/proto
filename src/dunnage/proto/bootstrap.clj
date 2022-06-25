(ns dunnage.proto.bootstrap
  (:require [clojure.java.io :as io])
  (:import com.google.protobuf.DescriptorProtos
           (com.google.protobuf DescriptorProtos$FileDescriptorSet DescriptorProtos$FileDescriptorSet))
  )


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