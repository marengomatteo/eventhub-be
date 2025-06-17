package com.eventhub.agenda_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventhub.agenda_service.entities.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, UUID> {

}