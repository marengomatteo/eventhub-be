package com.eventhub.event_service.service;

import net.devh.boot.grpc.client.inject.GrpcClient;

import com.eventhub.event_service.proto.GreeterGrpc;
import com.eventhub.event_service.proto.HelloRequest;
import com.eventhub.event_service.proto.HelloResponse;

public class GrpcClientService {
    
    @GrpcClient("local-grpc-server")
    private GreeterGrpc.GreeterBlockingStub greeterStub;
    
    public String chiamaServer(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloResponse reply = greeterStub.sayHello(request);
        return reply.getMessage();
    }
}
