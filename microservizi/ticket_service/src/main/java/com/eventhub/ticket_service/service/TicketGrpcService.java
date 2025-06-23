package com.eventhub.ticket_service.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.ticket_service.entities.Ticket;
import com.eventhub.ticket_service.repositories.TicketRepository;
import com.eventhub.ticket_service.proto.TicketGrpc;
import com.eventhub.ticket_service.proto.CreateTicketRequest;
import com.eventhub.ticket_service.proto.CreateTicketResponse;
import org.springframework.stereotype.Service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketGrpcService extends TicketGrpc.TicketImplBase {

    private final TicketRepository ticketRepository;

    @Override
    public void createTicket(CreateTicketRequest request, StreamObserver<CreateTicketResponse> responseObserver) {
        try {
            Ticket ticket = new Ticket();
            ticket.setUserId(request.getUserId());
            ticket.setUserName(request.getUserName());
            ticket.setUserSurname(request.getUserSurname());
            ticket.setUserEmail(request.getUserEmail());
            ticket.setEventName(request.getEventName());
            ticket.setEventId(request.getEventId());
            ticket.setStartDate(request.getStartDate());
            ticket.setStartTime(request.getStartTime());

            ticketRepository.save(ticket);

            CreateTicketResponse reply = CreateTicketResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        } catch (Exception e) {
            CreateTicketResponse reply = CreateTicketResponse.newBuilder()
                    .setSuccess(false)
                    .build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore interno al server");
        }
    }

}
