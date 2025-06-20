package com.eventhub.agenda_service.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eventhub.agenda_service.entities.Agenda;

public interface AgendaRepository extends MongoRepository<Agenda, String> {

    Optional<Agenda> findByEventId(String eventId);
}