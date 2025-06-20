package com.eventhub.event_service.service;

import org.springframework.stereotype.Service;

import com.eventhub.agenda_service.proto.AgendaGrpc;
import com.eventhub.agenda_service.proto.CreateAgendaRequest;
import com.eventhub.agenda_service.proto.CreateAgendaResponse;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class AgendaClientService {

    @GrpcClient("agenda-service")
    private AgendaGrpc.AgendaBlockingStub agendaStub;

    public Boolean creaAgenda(CreateAgendaRequest request) {
        try {
            CreateAgendaResponse response = agendaStub.createAgenda(request);

            return response.getSuccess();
        } catch (Exception e) {
            throw new RuntimeException("Errore nella chiamata gRPC: " + e.getMessage(), e);
        }
    }
}