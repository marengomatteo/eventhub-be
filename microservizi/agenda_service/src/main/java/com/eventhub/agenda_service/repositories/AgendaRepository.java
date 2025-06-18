package com.eventhub.agenda_service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eventhub.agenda_service.entities.Agenda;

public interface AgendaRepository extends MongoRepository<Agenda, String> {

}