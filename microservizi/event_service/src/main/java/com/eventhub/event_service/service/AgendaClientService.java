package com.eventhub.event_service.service;

import org.springframework.stereotype.Service;

import com.eventhub.agenda_service.proto.GreeterGrpc;
import com.eventhub.agenda_service.proto.HelloRequest;
import com.eventhub.agenda_service.proto.HelloResponse;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class AgendaClientService {

    @GrpcClient("agenda-service")
    private GreeterGrpc.GreeterBlockingStub agendaStub;

    public String chiamaAgenda(String parametro) {
        try {
            HelloRequest request = HelloRequest.newBuilder()
                    .setName(parametro)
                    .build();

            HelloResponse response = agendaStub.sayHello(request);

            return response.getMessage();
        } catch (Exception e) {
            throw new RuntimeException("Errore nella chiamata gRPC: " + e.getMessage(), e);
        }
    }
}