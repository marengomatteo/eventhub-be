// package com.eventhub.event_service.service;

// import net.devh.boot.grpc.client.inject.GrpcClient;

// import com.eventhub.event_service.proto.GreeterGrpc;

// public class GrpcClientService {
    
//     @GrpcClient("local-grpc-server")
//     private GreeterGrpc.GreeterBlockingStub greeterStub;
    
//     public String chiamaServer(String name) {
//         HelloRequest request = HelloRequest.newBuilder().setName(name).build();
//         HelloReply reply = greeterStub.sayHello(request);
//         return reply.getMessage();
//     }
// }
