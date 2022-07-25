(ns dunnage.proto.schema
  (:require [malli.core :as m]))

(def
  descriptor
  (m/schema
    [:schema {:registry
              {:google.protobuf/EnumValueOptions [:map
                                                  {}
                                                  [:deprecated {:protobuf/fieldnumber 1, :optional true} boolean?]
                                                  [:uninterpreted_option
                                                   {:protobuf/fieldnumber 999, :optional true}
                                                   [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf/ExtensionRangeOptions [:map
                                                       {}
                                                       [:uninterpreted_option
                                                        {:protobuf/fieldnumber 999, :optional true}
                                                        [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf.SourceCodeInfo/Location [:map
                                                         {}
                                                         [:path
                                                          {:protobuf/fieldnumber 1, :optional true}
                                                          [:sequential [:schema {:primitive "INT32"} int?]]]
                                                         [:span
                                                          {:protobuf/fieldnumber 2, :optional true}
                                                          [:sequential [:schema {:primitive "INT32"} int?]]]
                                                         [:leading_comments {:protobuf/fieldnumber 3, :optional true} string?]
                                                         [:trailing_comments {:protobuf/fieldnumber 4, :optional true} string?]
                                                         [:leading_detached_comments
                                                          {:protobuf/fieldnumber 6, :optional true}
                                                          [:sequential string?]]],
               :google.protobuf/OneofDescriptorProto [:map
                                                      {}
                                                      [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                      [:options
                                                       {:protobuf/fieldnumber 2, :optional true}
                                                       [:ref :google.protobuf/OneofOptions]]],
               :google.protobuf/OneofOptions [:map
                                              {}
                                              [:uninterpreted_option
                                               {:protobuf/fieldnumber 999, :optional true}
                                               [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf.FieldDescriptorProto/Label [:enum
                                                            ["LABEL_OPTIONAL" {:protobuf/fieldnumber 1}]
                                                            ["LABEL_REQUIRED" {:protobuf/fieldnumber 2}]
                                                            ["LABEL_REPEATED" {:protobuf/fieldnumber 3}]],
               :google.protobuf.FieldOptions/CType [:enum
                                                    ["STRING" {:protobuf/fieldnumber 0}]
                                                    ["CORD" {:protobuf/fieldnumber 1}]
                                                    ["STRING_PIECE" {:protobuf/fieldnumber 2}]],
               :google.protobuf/DescriptorProto [:map
                                                 {}
                                                 [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                 [:field
                                                  {:protobuf/fieldnumber 2, :optional true}
                                                  [:sequential [:ref :google.protobuf/FieldDescriptorProto]]]
                                                 [:extension
                                                  {:protobuf/fieldnumber 6, :optional true}
                                                  [:sequential [:ref :google.protobuf/FieldDescriptorProto]]]
                                                 [:nested_type
                                                  {:protobuf/fieldnumber 3, :optional true}
                                                  [:sequential [:ref :google.protobuf/DescriptorProto]]]
                                                 [:enum_type
                                                  {:protobuf/fieldnumber 4, :optional true}
                                                  [:sequential [:ref :google.protobuf/EnumDescriptorProto]]]
                                                 [:extension_range
                                                  {:protobuf/fieldnumber 5, :optional true}
                                                  [:sequential [:ref :google.protobuf.DescriptorProto/ExtensionRange]]]
                                                 [:oneof_decl
                                                  {:protobuf/fieldnumber 8, :optional true}
                                                  [:sequential [:ref :google.protobuf/OneofDescriptorProto]]]
                                                 [:options
                                                  {:protobuf/fieldnumber 7, :optional true}
                                                  [:ref :google.protobuf/MessageOptions]]
                                                 [:reserved_range
                                                  {:protobuf/fieldnumber 9, :optional true}
                                                  [:sequential [:ref :google.protobuf.DescriptorProto/ReservedRange]]]
                                                 [:reserved_name {:protobuf/fieldnumber 10, :optional true} [:sequential string?]]],
               :google.protobuf/GeneratedCodeInfo [:map
                                                   {}
                                                   [:annotation
                                                    {:protobuf/fieldnumber 1, :optional true}
                                                    [:sequential [:ref :google.protobuf.GeneratedCodeInfo/Annotation]]]],
               :google.protobuf.FileOptions/OptimizeMode [:enum
                                                          ["SPEED" {:protobuf/fieldnumber 1}]
                                                          ["CODE_SIZE" {:protobuf/fieldnumber 2}]
                                                          ["LITE_RUNTIME" {:protobuf/fieldnumber 3}]],
               :google.protobuf/ServiceOptions [:map
                                                {}
                                                [:deprecated {:protobuf/fieldnumber 33, :optional true} boolean?]
                                                [:uninterpreted_option
                                                 {:protobuf/fieldnumber 999, :optional true}
                                                 [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf.FieldDescriptorProto/Type [:enum
                                                           ["TYPE_DOUBLE" {:protobuf/fieldnumber 1}]
                                                           ["TYPE_FLOAT" {:protobuf/fieldnumber 2}]
                                                           ["TYPE_INT64" {:protobuf/fieldnumber 3}]
                                                           ["TYPE_UINT64" {:protobuf/fieldnumber 4}]
                                                           ["TYPE_INT32" {:protobuf/fieldnumber 5}]
                                                           ["TYPE_FIXED64" {:protobuf/fieldnumber 6}]
                                                           ["TYPE_FIXED32" {:protobuf/fieldnumber 7}]
                                                           ["TYPE_BOOL" {:protobuf/fieldnumber 8}]
                                                           ["TYPE_STRING" {:protobuf/fieldnumber 9}]
                                                           ["TYPE_GROUP" {:protobuf/fieldnumber 10}]
                                                           ["TYPE_MESSAGE" {:protobuf/fieldnumber 11}]
                                                           ["TYPE_BYTES" {:protobuf/fieldnumber 12}]
                                                           ["TYPE_UINT32" {:protobuf/fieldnumber 13}]
                                                           ["TYPE_ENUM" {:protobuf/fieldnumber 14}]
                                                           ["TYPE_SFIXED32" {:protobuf/fieldnumber 15}]
                                                           ["TYPE_SFIXED64" {:protobuf/fieldnumber 16}]
                                                           ["TYPE_SINT32" {:protobuf/fieldnumber 17}]
                                                           ["TYPE_SINT64" {:protobuf/fieldnumber 18}]],
               :google.protobuf/MethodDescriptorProto [:map
                                                       {}
                                                       [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                       [:input_type {:protobuf/fieldnumber 2, :optional true} string?]
                                                       [:output_type {:protobuf/fieldnumber 3, :optional true} string?]
                                                       [:options
                                                        {:protobuf/fieldnumber 4, :optional true}
                                                        [:ref :google.protobuf/MethodOptions]]
                                                       [:client_streaming {:protobuf/fieldnumber 5, :optional true} boolean?]
                                                       [:server_streaming {:protobuf/fieldnumber 6, :optional true} boolean?]],
               :google.protobuf/MethodOptions [:map
                                               {}
                                               [:deprecated {:protobuf/fieldnumber 33, :optional true} boolean?]
                                               [:idempotency_level
                                                {:protobuf/fieldnumber 34, :optional true}
                                                [:ref :google.protobuf.MethodOptions/IdempotencyLevel]]
                                               [:uninterpreted_option
                                                {:protobuf/fieldnumber 999, :optional true}
                                                [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf/MessageOptions [:map
                                                {}
                                                [:message_set_wire_format {:protobuf/fieldnumber 1, :optional true} boolean?]
                                                [:no_standard_descriptor_accessor {:protobuf/fieldnumber 2, :optional true} boolean?]
                                                [:deprecated {:protobuf/fieldnumber 3, :optional true} boolean?]
                                                [:map_entry {:protobuf/fieldnumber 7, :optional true} boolean?]
                                                [:uninterpreted_option
                                                 {:protobuf/fieldnumber 999, :optional true}
                                                 [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf/EnumOptions [:map
                                             {}
                                             [:allow_alias {:protobuf/fieldnumber 2, :optional true} boolean?]
                                             [:deprecated {:protobuf/fieldnumber 3, :optional true} boolean?]
                                             [:uninterpreted_option
                                              {:protobuf/fieldnumber 999, :optional true}
                                              [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf.MethodOptions/IdempotencyLevel [:enum
                                                                ["IDEMPOTENCY_UNKNOWN" {:protobuf/fieldnumber 0}]
                                                                ["NO_SIDE_EFFECTS" {:protobuf/fieldnumber 1}]
                                                                ["IDEMPOTENT" {:protobuf/fieldnumber 2}]],
               :google.protobuf/EnumDescriptorProto [:map
                                                     {}
                                                     [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                     [:value
                                                      {:protobuf/fieldnumber 2, :optional true}
                                                      [:sequential [:ref :google.protobuf/EnumValueDescriptorProto]]]
                                                     [:options
                                                      {:protobuf/fieldnumber 3, :optional true}
                                                      [:ref :google.protobuf/EnumOptions]]
                                                     [:reserved_range
                                                      {:protobuf/fieldnumber 4, :optional true}
                                                      [:sequential [:ref :google.protobuf.EnumDescriptorProto/EnumReservedRange]]]
                                                     [:reserved_name {:protobuf/fieldnumber 5, :optional true} [:sequential string?]]],
               :google.protobuf.UninterpretedOption/NamePart [:map
                                                              {}
                                                              [:name_part {:protobuf/fieldnumber 1} string?]
                                                              [:is_extension {:protobuf/fieldnumber 2} boolean?]],
               :google.protobuf.GeneratedCodeInfo/Annotation [:map
                                                              {}
                                                              [:path
                                                               {:protobuf/fieldnumber 1, :optional true}
                                                               [:sequential [:schema {:primitive "INT32"} int?]]]
                                                              [:source_file {:protobuf/fieldnumber 2, :optional true} string?]
                                                              [:begin
                                                               {:protobuf/fieldnumber 3, :optional true}
                                                               [:schema {:primitive "INT32"} int?]]
                                                              [:end
                                                               {:protobuf/fieldnumber 4, :optional true}
                                                               [:schema {:primitive "INT32"} int?]]],
               :google.protobuf/FileOptions [:map
                                             {}
                                             [:java_package {:protobuf/fieldnumber 1, :optional true} string?]
                                             [:java_outer_classname {:protobuf/fieldnumber 8, :optional true} string?]
                                             [:java_multiple_files {:protobuf/fieldnumber 10, :optional true} boolean?]
                                             [:java_generate_equals_and_hash {:protobuf/fieldnumber 20, :optional true} boolean?]
                                             [:java_string_check_utf8 {:protobuf/fieldnumber 27, :optional true} boolean?]
                                             [:optimize_for
                                              {:protobuf/fieldnumber 9, :optional true}
                                              [:ref :google.protobuf.FileOptions/OptimizeMode]]
                                             [:go_package {:protobuf/fieldnumber 11, :optional true} string?]
                                             [:cc_generic_services {:protobuf/fieldnumber 16, :optional true} boolean?]
                                             [:java_generic_services {:protobuf/fieldnumber 17, :optional true} boolean?]
                                             [:py_generic_services {:protobuf/fieldnumber 18, :optional true} boolean?]
                                             [:php_generic_services {:protobuf/fieldnumber 42, :optional true} boolean?]
                                             [:deprecated {:protobuf/fieldnumber 23, :optional true} boolean?]
                                             [:cc_enable_arenas {:protobuf/fieldnumber 31, :optional true} boolean?]
                                             [:objc_class_prefix {:protobuf/fieldnumber 36, :optional true} string?]
                                             [:csharp_namespace {:protobuf/fieldnumber 37, :optional true} string?]
                                             [:swift_prefix {:protobuf/fieldnumber 39, :optional true} string?]
                                             [:php_class_prefix {:protobuf/fieldnumber 40, :optional true} string?]
                                             [:php_namespace {:protobuf/fieldnumber 41, :optional true} string?]
                                             [:php_metadata_namespace {:protobuf/fieldnumber 44, :optional true} string?]
                                             [:ruby_package {:protobuf/fieldnumber 45, :optional true} string?]
                                             [:uninterpreted_option
                                              {:protobuf/fieldnumber 999, :optional true}
                                              [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf/UninterpretedOption [:map
                                                     {}
                                                     [:name
                                                      {:protobuf/fieldnumber 2, :optional true}
                                                      [:sequential [:ref :google.protobuf.UninterpretedOption/NamePart]]]
                                                     [:identifier_value {:protobuf/fieldnumber 3, :optional true} string?]
                                                     [:positive_int_value
                                                      {:protobuf/fieldnumber 4, :optional true}
                                                      [:schema {:primitive "UINT64"} int?]]
                                                     [:negative_int_value
                                                      {:protobuf/fieldnumber 5, :optional true}
                                                      [:schema {:primitive "INT64"} int?]]
                                                     [:double_value {:protobuf/fieldnumber 6, :optional true} double?]
                                                     [:string_value {:protobuf/fieldnumber 7, :optional true} bytes?]
                                                     [:aggregate_value {:protobuf/fieldnumber 8, :optional true} string?]],
               :google.protobuf/SourceCodeInfo [:map
                                                {}
                                                [:location
                                                 {:protobuf/fieldnumber 1, :optional true}
                                                 [:sequential [:ref :google.protobuf.SourceCodeInfo/Location]]]],
               :google.protobuf/FileDescriptorProto [:map
                                                     {}
                                                     [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                     [:package {:protobuf/fieldnumber 2, :optional true} string?]
                                                     [:dependency {:protobuf/fieldnumber 3, :optional true} [:sequential string?]]
                                                     [:public_dependency
                                                      {:protobuf/fieldnumber 10, :optional true}
                                                      [:sequential [:schema {:primitive "INT32"} int?]]]
                                                     [:weak_dependency
                                                      {:protobuf/fieldnumber 11, :optional true}
                                                      [:sequential [:schema {:primitive "INT32"} int?]]]
                                                     [:message_type
                                                      {:protobuf/fieldnumber 4, :optional true}
                                                      [:sequential [:ref :google.protobuf/DescriptorProto]]]
                                                     [:enum_type
                                                      {:protobuf/fieldnumber 5, :optional true}
                                                      [:sequential [:ref :google.protobuf/EnumDescriptorProto]]]
                                                     [:service
                                                      {:protobuf/fieldnumber 6, :optional true}
                                                      [:sequential [:ref :google.protobuf/ServiceDescriptorProto]]]
                                                     [:extension
                                                      {:protobuf/fieldnumber 7, :optional true}
                                                      [:sequential [:ref :google.protobuf/FieldDescriptorProto]]]
                                                     [:options
                                                      {:protobuf/fieldnumber 8, :optional true}
                                                      [:ref :google.protobuf/FileOptions]]
                                                     [:source_code_info
                                                      {:protobuf/fieldnumber 9, :optional true}
                                                      [:ref :google.protobuf/SourceCodeInfo]]
                                                     [:syntax {:protobuf/fieldnumber 12, :optional true} string?]],
               :google.protobuf/EnumValueDescriptorProto [:map
                                                          {}
                                                          [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                          [:number
                                                           {:protobuf/fieldnumber 2, :optional true}
                                                           [:schema {:primitive "INT32"} int?]]
                                                          [:options
                                                           {:protobuf/fieldnumber 3, :optional true}
                                                           [:ref :google.protobuf/EnumValueOptions]]],
               :google.protobuf.DescriptorProto/ReservedRange [:map
                                                               {}
                                                               [:start
                                                                {:protobuf/fieldnumber 1, :optional true}
                                                                [:schema {:primitive "INT32"} int?]]
                                                               [:end
                                                                {:protobuf/fieldnumber 2, :optional true}
                                                                [:schema {:primitive "INT32"} int?]]],
               :google.protobuf/ServiceDescriptorProto [:map
                                                        {}
                                                        [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                        [:method
                                                         {:protobuf/fieldnumber 2, :optional true}
                                                         [:sequential [:ref :google.protobuf/MethodDescriptorProto]]]
                                                        [:options
                                                         {:protobuf/fieldnumber 3, :optional true}
                                                         [:ref :google.protobuf/ServiceOptions]]],
               :google.protobuf.EnumDescriptorProto/EnumReservedRange [:map
                                                                       {}
                                                                       [:start
                                                                        {:protobuf/fieldnumber 1, :optional true}
                                                                        [:schema {:primitive "INT32"} int?]]
                                                                       [:end
                                                                        {:protobuf/fieldnumber 2, :optional true}
                                                                        [:schema {:primitive "INT32"} int?]]],
               :google.protobuf/FileDescriptorSet [:map
                                                   {}
                                                   [:file
                                                    {:protobuf/fieldnumber 1, :optional true}
                                                    [:sequential [:ref :google.protobuf/FileDescriptorProto]]]],
               :google.protobuf/FieldDescriptorProto [:map
                                                      {}
                                                      [:name {:protobuf/fieldnumber 1, :optional true} string?]
                                                      [:number
                                                       {:protobuf/fieldnumber 3, :optional true}
                                                       [:schema {:primitive "INT32"} int?]]
                                                      [:label
                                                       {:protobuf/fieldnumber 4, :optional true}
                                                       [:ref :google.protobuf.FieldDescriptorProto/Label]]
                                                      [:type
                                                       {:protobuf/fieldnumber 5, :optional true}
                                                       [:ref :google.protobuf.FieldDescriptorProto/Type]]
                                                      [:type_name {:protobuf/fieldnumber 6, :optional true} string?]
                                                      [:extendee {:protobuf/fieldnumber 2, :optional true} string?]
                                                      [:default_value {:protobuf/fieldnumber 7, :optional true} string?]
                                                      [:oneof_index
                                                       {:protobuf/fieldnumber 9, :optional true}
                                                       [:schema {:primitive "INT32"} int?]]
                                                      [:json_name {:protobuf/fieldnumber 10, :optional true} string?]
                                                      [:options
                                                       {:protobuf/fieldnumber 8, :optional true}
                                                       [:ref :google.protobuf/FieldOptions]]
                                                      [:proto3_optional {:protobuf/fieldnumber 17, :optional true} boolean?]],
               :google.protobuf.FieldOptions/JSType [:enum
                                                     ["JS_NORMAL" {:protobuf/fieldnumber 0}]
                                                     ["JS_STRING" {:protobuf/fieldnumber 1}]
                                                     ["JS_NUMBER" {:protobuf/fieldnumber 2}]],
               :google.protobuf/FieldOptions [:map
                                              {}
                                              [:ctype
                                               {:protobuf/fieldnumber 1, :optional true}
                                               [:ref :google.protobuf.FieldOptions/CType]]
                                              [:packed {:protobuf/fieldnumber 2, :optional true} boolean?]
                                              [:jstype
                                               {:protobuf/fieldnumber 6, :optional true}
                                               [:ref :google.protobuf.FieldOptions/JSType]]
                                              [:lazy {:protobuf/fieldnumber 5, :optional true} boolean?]
                                              [:unverified_lazy {:protobuf/fieldnumber 15, :optional true} boolean?]
                                              [:deprecated {:protobuf/fieldnumber 3, :optional true} boolean?]
                                              [:weak {:protobuf/fieldnumber 10, :optional true} boolean?]
                                              [:uninterpreted_option
                                               {:protobuf/fieldnumber 999, :optional true}
                                               [:sequential [:ref :google.protobuf/UninterpretedOption]]]],
               :google.protobuf.DescriptorProto/ExtensionRange [:map
                                                                {}
                                                                [:start
                                                                 {:protobuf/fieldnumber 1, :optional true}
                                                                 [:schema {:primitive "INT32"} int?]]
                                                                [:end
                                                                 {:protobuf/fieldnumber 2, :optional true}
                                                                 [:schema {:primitive "INT32"} int?]]
                                                                [:options
                                                                 {:protobuf/fieldnumber 3, :optional true}
                                                                 [:ref :google.protobuf/ExtensionRangeOptions]]]}}
     :google.protobuf/FileDescriptorSet]))


