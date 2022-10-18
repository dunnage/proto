
bin/protoc --descriptor_set_out=dev-resources/transport_security_common.descriptorset \
           --proto_path=dev-resources \
           --include_source_info dev-resources/src/proto/grpc/gcp/transport_security_common.proto
bin/protoc --descriptor_set_out=dev-resources/altscontext.descriptorset \
           --proto_path=dev-resources \
           --include_source_info dev-resources/src/proto/grpc/gcp/altscontext.proto
bin/protoc --descriptor_set_out=dev-resources/handshaker.descriptorset \
           --proto_path=dev-resources \
           --include_source_info dev-resources/src/proto/grpc/gcp/handshaker.proto
