(ns dunnage.proto.schema
  (:require [malli.core :as m]))

(def
  descriptor
  (m/schema
    [:schema {:registry
              {:google.protobuf/EnumValueOptions [:map
                                                  {}
                                                  [:uninterpreted_option
                                                   {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                                   [:sequential [:ref :google.protobuf/UninterpretedOption]]]
                                                  [:deprecated
                                                   {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_BOOL"}
                                                   boolean?]],
               :google.protobuf/ExtensionRangeOptions [:map
                                                       {}
                                                       [:uninterpreted_option
                                                        {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                                        [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf.SourceCodeInfo/Location [:map
                                                         {}
                                                         [:leading_detached_comments
                                                          {:protobuf/fieldnumber 6, :optional true, :primitive "TYPE_STRING"}
                                                          [:sequential string?]]
                                                         [:trailing_comments
                                                          {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_STRING"}
                                                          string?]
                                                         [:leading_comments
                                                          {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_STRING"}
                                                          string?]
                                                         [:span
                                                          {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_INT32"}
                                                          [:sequential int?]]
                                                         [:path
                                                          {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_INT32"}
                                                          [:sequential int?]]],
               :google.protobuf/OneofDescriptorProto [:map
                                                      {}
                                                      [:options
                                                       {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_MESSAGE"}
                                                       [:ref :google.protobuf/OneofOptions]]
                                                      [:name
                                                       {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                                       string?]],
               :google.protobuf/OneofOptions [:map
                                              {}
                                              [:uninterpreted_option
                                               {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                               [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf.FieldDescriptorProto/Label [:enum
                                                            ["LABEL_REPEATED" {:protobuf/fieldnumber 3}]
                                                            ["LABEL_REQUIRED" {:protobuf/fieldnumber 2}]
                                                            ["LABEL_OPTIONAL" {:protobuf/fieldnumber 1}]],
               :google.protobuf.FieldOptions/CType [:enum
                                                    ["STRING_PIECE" {:protobuf/fieldnumber 2}]
                                                    ["CORD" {:protobuf/fieldnumber 1}]
                                                    ["STRING" {:protobuf/fieldnumber 0}]],
               :google.protobuf/DescriptorProto [:map
                                                 {}
                                                 [:reserved_name
                                                  {:protobuf/fieldnumber 10, :optional true, :primitive "TYPE_STRING"}
                                                  [:sequential string?]]
                                                 [:reserved_range
                                                  {:protobuf/fieldnumber 9, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:sequential [:ref :google.protobuf.DescriptorProto/ReservedRange]]]
                                                 [:options
                                                  {:protobuf/fieldnumber 7, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:ref :google.protobuf/MessageOptions]]
                                                 [:oneof_decl
                                                  {:protobuf/fieldnumber 8, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:sequential [:ref :google.protobuf/OneofDescriptorProto]]]
                                                 [:extension_range
                                                  {:protobuf/fieldnumber 5, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:sequential [:ref :google.protobuf.DescriptorProto/ExtensionRange]]]
                                                 [:enum_type
                                                  {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:sequential [:ref :google.protobuf/EnumDescriptorProto]]]
                                                 [:nested_type
                                                  {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:sequential [:ref :google.protobuf/DescriptorProto]]]
                                                 [:extension
                                                  {:protobuf/fieldnumber 6, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:sequential [:ref :google.protobuf/FieldDescriptorProto]]]
                                                 [:field
                                                  {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_MESSAGE"}
                                                  [:sequential [:ref :google.protobuf/FieldDescriptorProto]]]
                                                 [:name {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"} string?]],
               :google.protobuf/GeneratedCodeInfo [:map
                                                   {}
                                                   [:annotation
                                                    {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_MESSAGE"}
                                                    [:sequential [:ref :google.protobuf.GeneratedCodeInfo/Annotation]]]],
               :google.protobuf.FileOptions/OptimizeMode [:enum
                                                          ["LITE_RUNTIME" {:protobuf/fieldnumber 3}]
                                                          ["CODE_SIZE" {:protobuf/fieldnumber 2}]
                                                          ["SPEED" {:protobuf/fieldnumber 1}]],
               :google.protobuf/ServiceOptions [:map
                                                {}
                                                [:uninterpreted_option
                                                 {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                                 [:sequential [:ref :google.protobuf/UninterpretedOption]]]
                                                [:deprecated
                                                 {:protobuf/fieldnumber 33, :optional true, :primitive "TYPE_BOOL"}
                                                 boolean?]],
               :google.protobuf.FieldDescriptorProto/Type [:enum
                                                           ["TYPE_SINT64" {:protobuf/fieldnumber 18}]
                                                           ["TYPE_SINT32" {:protobuf/fieldnumber 17}]
                                                           ["TYPE_SFIXED64" {:protobuf/fieldnumber 16}]
                                                           ["TYPE_SFIXED32" {:protobuf/fieldnumber 15}]
                                                           ["TYPE_ENUM" {:protobuf/fieldnumber 14}]
                                                           ["TYPE_UINT32" {:protobuf/fieldnumber 13}]
                                                           ["TYPE_BYTES" {:protobuf/fieldnumber 12}]
                                                           ["TYPE_MESSAGE" {:protobuf/fieldnumber 11}]
                                                           ["TYPE_GROUP" {:protobuf/fieldnumber 10}]
                                                           ["TYPE_STRING" {:protobuf/fieldnumber 9}]
                                                           ["TYPE_BOOL" {:protobuf/fieldnumber 8}]
                                                           ["TYPE_FIXED32" {:protobuf/fieldnumber 7}]
                                                           ["TYPE_FIXED64" {:protobuf/fieldnumber 6}]
                                                           ["TYPE_INT32" {:protobuf/fieldnumber 5}]
                                                           ["TYPE_UINT64" {:protobuf/fieldnumber 4}]
                                                           ["TYPE_INT64" {:protobuf/fieldnumber 3}]
                                                           ["TYPE_FLOAT" {:protobuf/fieldnumber 2}]
                                                           ["TYPE_DOUBLE" {:protobuf/fieldnumber 1}]],
               :google.protobuf/MethodDescriptorProto [:map
                                                       {}
                                                       [:server_streaming
                                                        {:protobuf/fieldnumber 6, :optional true, :primitive "TYPE_BOOL"}
                                                        boolean?]
                                                       [:client_streaming
                                                        {:protobuf/fieldnumber 5, :optional true, :primitive "TYPE_BOOL"}
                                                        boolean?]
                                                       [:options
                                                        {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_MESSAGE"}
                                                        [:ref :google.protobuf/MethodOptions]]
                                                       [:output_type
                                                        {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_STRING"}
                                                        string?]
                                                       [:input_type
                                                        {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_STRING"}
                                                        string?]
                                                       [:name
                                                        {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                                        string?]],
               :google.protobuf/MethodOptions [:map
                                               {}
                                               [:uninterpreted_option
                                                {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                                [:sequential [:ref :google.protobuf/UninterpretedOption]]]
                                               [:idempotency_level
                                                {:protobuf/fieldnumber 34, :optional true, :primitive "TYPE_ENUM"}
                                                [:ref :google.protobuf.MethodOptions/IdempotencyLevel]]
                                               [:deprecated
                                                {:protobuf/fieldnumber 33, :optional true, :primitive "TYPE_BOOL"}
                                                boolean?]],
               :google.protobuf/MessageOptions [:map
                                                {}
                                                [:uninterpreted_option
                                                 {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                                 [:sequential [:ref :google.protobuf/UninterpretedOption]]]
                                                [:map_entry
                                                 {:protobuf/fieldnumber 7, :optional true, :primitive "TYPE_BOOL"}
                                                 boolean?]
                                                [:deprecated
                                                 {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_BOOL"}
                                                 boolean?]
                                                [:no_standard_descriptor_accessor
                                                 {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_BOOL"}
                                                 boolean?]
                                                [:message_set_wire_format
                                                 {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_BOOL"}
                                                 boolean?]],
               :google.protobuf/EnumOptions [:map
                                             {}
                                             [:uninterpreted_option
                                              {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                              [:sequential [:ref :google.protobuf/UninterpretedOption]]]
                                             [:deprecated {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_BOOL"} boolean?]
                                             [:allow_alias {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_BOOL"} boolean?]],
               :google.protobuf.MethodOptions/IdempotencyLevel [:enum
                                                                ["IDEMPOTENT" {:protobuf/fieldnumber 2}]
                                                                ["NO_SIDE_EFFECTS" {:protobuf/fieldnumber 1}]
                                                                ["IDEMPOTENCY_UNKNOWN" {:protobuf/fieldnumber 0}]],
               :google.protobuf/EnumDescriptorProto [:map
                                                     {}
                                                     [:reserved_name
                                                      {:protobuf/fieldnumber 5, :optional true, :primitive "TYPE_STRING"}
                                                      [:sequential string?]]
                                                     [:reserved_range
                                                      {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:sequential [:ref :google.protobuf.EnumDescriptorProto/EnumReservedRange]]]
                                                     [:options
                                                      {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:ref :google.protobuf/EnumOptions]]
                                                     [:value
                                                      {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:sequential [:ref :google.protobuf/EnumValueDescriptorProto]]]
                                                     [:name
                                                      {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                                      string?]],
               :google.protobuf.UninterpretedOption/NamePart [:map
                                                              {}
                                                              [:is_extension
                                                               {:protobuf/fieldnumber 2, :primitive "TYPE_BOOL"}
                                                               boolean?]
                                                              [:name_part {:protobuf/fieldnumber 1, :primitive "TYPE_STRING"} string?]],
               :google.protobuf.GeneratedCodeInfo/Annotation [:map
                                                              {}
                                                              [:end
                                                               {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_INT32"}
                                                               int?]
                                                              [:begin
                                                               {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_INT32"}
                                                               int?]
                                                              [:source_file
                                                               {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_STRING"}
                                                               string?]
                                                              [:path
                                                               {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_INT32"}
                                                               [:sequential int?]]],
               :google.protobuf/FileOptions [:map
                                             {}
                                             [:uninterpreted_option
                                              {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                              [:sequential [:ref :google.protobuf/UninterpretedOption]]]
                                             [:ruby_package
                                              {:protobuf/fieldnumber 45, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:php_metadata_namespace
                                              {:protobuf/fieldnumber 44, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:php_namespace
                                              {:protobuf/fieldnumber 41, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:php_class_prefix
                                              {:protobuf/fieldnumber 40, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:swift_prefix
                                              {:protobuf/fieldnumber 39, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:csharp_namespace
                                              {:protobuf/fieldnumber 37, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:objc_class_prefix
                                              {:protobuf/fieldnumber 36, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:cc_enable_arenas
                                              {:protobuf/fieldnumber 31, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:deprecated {:protobuf/fieldnumber 23, :optional true, :primitive "TYPE_BOOL"} boolean?]
                                             [:php_generic_services
                                              {:protobuf/fieldnumber 42, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:py_generic_services
                                              {:protobuf/fieldnumber 18, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:java_generic_services
                                              {:protobuf/fieldnumber 17, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:cc_generic_services
                                              {:protobuf/fieldnumber 16, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:go_package
                                              {:protobuf/fieldnumber 11, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:optimize_for
                                              {:protobuf/fieldnumber 9, :optional true, :primitive "TYPE_ENUM"}
                                              [:ref :google.protobuf.FileOptions/OptimizeMode]]
                                             [:java_string_check_utf8
                                              {:protobuf/fieldnumber 27, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:java_generate_equals_and_hash
                                              {:protobuf/fieldnumber 20, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:java_multiple_files
                                              {:protobuf/fieldnumber 10, :optional true, :primitive "TYPE_BOOL"}
                                              boolean?]
                                             [:java_outer_classname
                                              {:protobuf/fieldnumber 8, :optional true, :primitive "TYPE_STRING"}
                                              string?]
                                             [:java_package
                                              {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                              string?]],
               :google.protobuf/UninterpretedOption [:map
                                                     {}
                                                     [:aggregate_value
                                                      {:protobuf/fieldnumber 8, :optional true, :primitive "TYPE_STRING"}
                                                      string?]
                                                     [:string_value
                                                      {:protobuf/fieldnumber 7, :optional true, :primitive "TYPE_BYTES"}
                                                      bytes?]
                                                     [:double_value
                                                      {:protobuf/fieldnumber 6, :optional true, :primitive "TYPE_DOUBLE"}
                                                      double?]
                                                     [:negative_int_value
                                                      {:protobuf/fieldnumber 5, :optional true, :primitive "TYPE_INT64"}
                                                      int?]
                                                     [:positive_int_value
                                                      {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_UINT64"}
                                                      pos-int?]
                                                     [:identifier_value
                                                      {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_STRING"}
                                                      string?]
                                                     [:name
                                                      {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:sequential [:ref :google.protobuf.UninterpretedOption/NamePart]]]],
               :google.protobuf/SourceCodeInfo [:map
                                                {}
                                                [:location
                                                 {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_MESSAGE"}
                                                 [:sequential [:ref :google.protobuf.SourceCodeInfo/Location]]]],
               :google.protobuf/FileDescriptorProto [:map
                                                     {}
                                                     [:syntax
                                                      {:protobuf/fieldnumber 12, :optional true, :primitive "TYPE_STRING"}
                                                      string?]
                                                     [:source_code_info
                                                      {:protobuf/fieldnumber 9, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:ref :google.protobuf/SourceCodeInfo]]
                                                     [:options
                                                      {:protobuf/fieldnumber 8, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:ref :google.protobuf/FileOptions]]
                                                     [:extension
                                                      {:protobuf/fieldnumber 7, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:sequential [:ref :google.protobuf/FieldDescriptorProto]]]
                                                     [:service
                                                      {:protobuf/fieldnumber 6, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:sequential [:ref :google.protobuf/ServiceDescriptorProto]]]
                                                     [:enum_type
                                                      {:protobuf/fieldnumber 5, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:sequential [:ref :google.protobuf/EnumDescriptorProto]]]
                                                     [:message_type
                                                      {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_MESSAGE"}
                                                      [:sequential [:ref :google.protobuf/DescriptorProto]]]
                                                     [:weak_dependency
                                                      {:protobuf/fieldnumber 11, :optional true, :primitive "TYPE_INT32"}
                                                      [:sequential int?]]
                                                     [:public_dependency
                                                      {:protobuf/fieldnumber 10, :optional true, :primitive "TYPE_INT32"}
                                                      [:sequential int?]]
                                                     [:dependency
                                                      {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_STRING"}
                                                      [:sequential string?]]
                                                     [:package
                                                      {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_STRING"}
                                                      string?]
                                                     [:name
                                                      {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                                      string?]],
               :google.protobuf/EnumValueDescriptorProto [:map
                                                          {}
                                                          [:options
                                                           {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_MESSAGE"}
                                                           [:ref :google.protobuf/EnumValueOptions]]
                                                          [:number
                                                           {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_INT32"}
                                                           int?]
                                                          [:name
                                                           {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                                           string?]],
               :google.protobuf.DescriptorProto/ReservedRange [:map
                                                               {}
                                                               [:end
                                                                {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_INT32"}
                                                                int?]
                                                               [:start
                                                                {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_INT32"}
                                                                int?]],
               :google.protobuf/ServiceDescriptorProto [:map
                                                        {}
                                                        [:options
                                                         {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_MESSAGE"}
                                                         [:ref :google.protobuf/ServiceOptions]]
                                                        [:method
                                                         {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_MESSAGE"}
                                                         [:sequential [:ref :google.protobuf/MethodDescriptorProto]]]
                                                        [:name
                                                         {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                                         string?]],
               :google.protobuf.EnumDescriptorProto/EnumReservedRange [:map
                                                                       {}
                                                                       [:end
                                                                        {:protobuf/fieldnumber 2,
                                                                         :optional true,
                                                                         :primitive "TYPE_INT32"}
                                                                        int?]
                                                                       [:start
                                                                        {:protobuf/fieldnumber 1,
                                                                         :optional true,
                                                                         :primitive "TYPE_INT32"}
                                                                        int?]],
               :google.protobuf/FileDescriptorSet [:map
                                                   {}
                                                   [:file
                                                    {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_MESSAGE"}
                                                    [:sequential [:ref :google.protobuf/FileDescriptorProto]]]],
               :google.protobuf/FieldDescriptorProto [:map
                                                      {}
                                                      [:proto3_optional
                                                       {:protobuf/fieldnumber 17, :optional true, :primitive "TYPE_BOOL"}
                                                       boolean?]
                                                      [:options
                                                       {:protobuf/fieldnumber 8, :optional true, :primitive "TYPE_MESSAGE"}
                                                       [:ref :google.protobuf/FieldOptions]]
                                                      [:json_name
                                                       {:protobuf/fieldnumber 10, :optional true, :primitive "TYPE_STRING"}
                                                       string?]
                                                      [:oneof_index
                                                       {:protobuf/fieldnumber 9, :optional true, :primitive "TYPE_INT32"}
                                                       int?]
                                                      [:default_value
                                                       {:protobuf/fieldnumber 7, :optional true, :primitive "TYPE_STRING"}
                                                       string?]
                                                      [:extendee
                                                       {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_STRING"}
                                                       string?]
                                                      [:type_name
                                                       {:protobuf/fieldnumber 6, :optional true, :primitive "TYPE_STRING"}
                                                       string?]
                                                      [:type
                                                       {:protobuf/fieldnumber 5, :optional true, :primitive "TYPE_ENUM"}
                                                       [:ref :google.protobuf.FieldDescriptorProto/Type]]
                                                      [:label
                                                       {:protobuf/fieldnumber 4, :optional true, :primitive "TYPE_ENUM"}
                                                       [:ref :google.protobuf.FieldDescriptorProto/Label]]
                                                      [:number
                                                       {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_INT32"}
                                                       int?]
                                                      [:name
                                                       {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_STRING"}
                                                       string?]],
               :google.protobuf.FieldOptions/JSType [:enum
                                                     ["JS_NUMBER" {:protobuf/fieldnumber 2}]
                                                     ["JS_STRING" {:protobuf/fieldnumber 1}]
                                                     ["JS_NORMAL" {:protobuf/fieldnumber 0}]],
               :google.protobuf/FieldOptions [:map
                                              {}
                                              [:uninterpreted_option
                                               {:protobuf/fieldnumber 999, :optional true, :primitive "TYPE_MESSAGE"}
                                               [:sequential [:ref :google.protobuf/UninterpretedOption]]]
                                              [:weak {:protobuf/fieldnumber 10, :optional true, :primitive "TYPE_BOOL"} boolean?]
                                              [:deprecated {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_BOOL"} boolean?]
                                              [:unverified_lazy
                                               {:protobuf/fieldnumber 15, :optional true, :primitive "TYPE_BOOL"}
                                               boolean?]
                                              [:lazy {:protobuf/fieldnumber 5, :optional true, :primitive "TYPE_BOOL"} boolean?]
                                              [:jstype
                                               {:protobuf/fieldnumber 6, :optional true, :primitive "TYPE_ENUM"}
                                               [:ref :google.protobuf.FieldOptions/JSType]]
                                              [:packed {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_BOOL"} boolean?]
                                              [:ctype
                                               {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_ENUM"}
                                               [:ref :google.protobuf.FieldOptions/CType]]],
               :google.protobuf.DescriptorProto/ExtensionRange [:map
                                                                {}
                                                                [:options
                                                                 {:protobuf/fieldnumber 3, :optional true, :primitive "TYPE_MESSAGE"}
                                                                 [:ref :google.protobuf/ExtensionRangeOptions]]
                                                                [:end
                                                                 {:protobuf/fieldnumber 2, :optional true, :primitive "TYPE_INT32"}
                                                                 int?]
                                                                [:start
                                                                 {:protobuf/fieldnumber 1, :optional true, :primitive "TYPE_INT32"}
                                                                 int?]]}}
     :google.protobuf/FileDescriptorSet]))


