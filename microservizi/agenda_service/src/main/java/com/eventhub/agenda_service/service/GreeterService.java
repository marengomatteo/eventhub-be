package com.eventhub.agenda_service.service;

import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;

import com.eventhub.agenda_service.proto.GreeterGrpc;
import com.eventhub.agenda_service.proto.HelloRequest;
import com.eventhub.agenda_service.proto.HelloResponse;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String message = "Ciao, " + request.getName();
        HelloResponse reply = HelloResponse.newBuilder()
                .setMessage(message)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
