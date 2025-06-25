package com.eventhub.agenda_service.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.agenda_service.entities.Agenda;
import com.eventhub.agenda_service.proto.AgendaGrpc;
import com.eventhub.agenda_service.proto.CreateAgendaRequest;
import com.eventhub.agenda_service.proto.CreateAgendaResponse;
import com.eventhub.agenda_service.repositories.AgendaRepository;
import org.springframework.stereotype.Service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendaGrpcService extends AgendaGrpc.AgendaImplBase {

    private final AgendaRepository agendaRepository;

    @Override
    public void createAgenda(CreateAgendaRequest request, StreamObserver<CreateAgendaResponse> responseObserver) {
        try {
            Agenda agenda = new Agenda();
            agenda.setEventName(request.getEventName());
            agenda.setEventId(request.getEventId());
            agenda.setDay(stringToLocalDate(request.getDay()));
            agenda.setSessions(new ArrayList<>());
            Agenda agendaSaved = agendaRepository.save(agenda);

            CreateAgendaResponse reply = CreateAgendaResponse.newBuilder()
                    .setSuccess(true)
                    .setAgendaId(agendaSaved.getId().toString())
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } catch (Exception e) {
            CreateAgendaResponse reply = CreateAgendaResponse.newBuilder()
                    .setSuccess(false)
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore interno al server");
        }
    }

    private LocalDate stringToLocalDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected: yyyy-MM-dd, got: " + dateString, e);
        }
    }
}
