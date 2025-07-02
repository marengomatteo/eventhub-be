package com.eventhub.event_service.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.event_service.entities.Event;
import com.eventhub.event_service.entities.Participant;
import com.eventhub.event_service.repositories.EventRepository;
import com.eventhub.ticket_service.proto.CreateTicketRequest;
import com.eventhub.ticket_service.proto.CreateTicketResponse;
import com.eventhub.ticket_service.proto.TicketGrpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketClientService {

    private final EventRepository eventRepository;

    @GrpcClient("ticket-service")
    private TicketGrpc.TicketBlockingStub ticketStub;

    public void addParticipant(String id, Participant participant) {
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> {
                log.error("Event with id {} not found for update", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Evento non trovato");
            });

            List<Participant> partecipanti = event.getPartecipantsList();
            partecipanti.add(participant);

            event.setPartecipantsList(partecipanti);

            CreateTicketRequest request = createTicketRequest(event, participant);
            Boolean success = creaTicket(request);
            if (success) {
                eventRepository.save(event);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Qualcosa è andato storto con la registrazione, controlla i dati e riprova");
            }

        } catch (DataAccessException dae) {
            dae.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

    private CreateTicketRequest createTicketRequest(Event event, Participant participant) {
        CreateTicketRequest request = CreateTicketRequest.newBuilder()
                .setUserId(participant.getUserId())
                .setUserName(participant.getName())
                .setUserSurname(participant.getSurname())
                .setUserEmail(participant.getEmail())
                .setEventId(event.getId())
                .setEventName(event.getEventName())
                .setStartDate(event.getStartTime().toString())
                .setLocation(event.getLocation())
                .build();
        return request;
    }

    public Boolean creaTicket(CreateTicketRequest request) {
        try {
            CreateTicketResponse response = ticketStub.createTicket(request);

            return response.getSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }
}