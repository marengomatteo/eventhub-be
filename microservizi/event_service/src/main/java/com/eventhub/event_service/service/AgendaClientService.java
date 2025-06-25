package com.eventhub.event_service.service;

import org.springframework.stereotype.Service;

import com.eventhub.agenda_service.proto.AgendaGrpc;
import com.eventhub.agenda_service.proto.CreateAgendaRequest;
import com.eventhub.agenda_service.proto.CreateAgendaResponse;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
@Slf4j
public class AgendaClientService {

    @GrpcClient("agenda-service")
    private AgendaGrpc.AgendaBlockingStub agendaStub;

    public CreateAgendaResponse creaAgenda(CreateAgendaRequest request) {
        try {
            CreateAgendaResponse response = agendaStub.createAgenda(request);

            return response;
        } catch (Exception e) {
            log.error("Error creating new agenda: ", e);
            throw new RuntimeException("Errore nella chiamata gRPC: " + e.getMessage(), e);
        }
    }
}