package com.eventhub.ticket_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventhub.ticket_service.entities.Ticket;
import com.eventhub.ticket_service.repositories.TicketRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Ticket> getTicketsByUserId(String userId) {
        return ticketRepository.findByUserId(userId);
    }
}
