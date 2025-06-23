package com.eventhub.ticket_service.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eventhub.ticket_service.entities.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByUserId(String userId);

}