package com.eventhub.agenda_service.service;

import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.eventhub.agenda_service.dto.AgendaRequest;
import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.entities.Agenda;
import com.eventhub.agenda_service.repositories.AgendaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaResponse getAgendaByEvent(String id) {
        try {
            Agenda agenda = agendaRepository.findById(UUID.fromString(id)).orElseThrow(() -> {
                log.error("Agenda with event id {} not found", id);
                throw new RuntimeException("Agenda not found");
            });
            AgendaResponse agendaResponse = new AgendaResponse();
            agendaResponse.setId(agenda.getId());
            agendaResponse.setEventId(agenda.getEventId());

            return agendaResponse;
        } catch (Exception e) {
            log.error("Error retrieving agenda for event with id {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to retrieve agenda for event", e);
        }
    }

    @Transactional
    public String newAgenda(AgendaRequest request) {
        try {
            Agenda agenda = new Agenda();
            agenda.setEventId(request.getEventId());
            Agenda agendaSaved = agendaRepository.save(agenda);
            return agendaSaved.getId().toString();

        } catch (DataAccessException e) {
            log.error("Error creating new event: ", e);
            throw new RuntimeException("Failed to create new event", e);
        }
    }

    @Transactional
    public String updateAgenda(String id, AgendaRequest request) {
        try {

            Agenda agenda = agendaRepository.findById(UUID.fromString(id)).orElseThrow(() -> {
                log.error("Error creating new event: ");
                throw new RuntimeException("Failed to create new event");
            });

            agenda.setEventId(request.getEventId());
            Agenda agendaSaved = agendaRepository.save(agenda);
            return agendaSaved.getId().toString();

        } catch (DataAccessException e) {
            log.error("Error creating new event: ", e);
            throw new RuntimeException("Failed to create new event", e);
        }
    }

    @Transactional
    public void deleteAgenda(String id) {
        try {
            Agenda a = agendaRepository.findById(UUID.fromString(id)).orElseThrow(() -> {
                log.error("Error creating new event: ");
                throw new RuntimeException("Failed to create new event");
            });
            agendaRepository.delete(a);

        } catch (DataAccessException e) {
            log.error("Error creating new event: ", e);
            throw new RuntimeException("Failed to create new event", e);
        }
    }
}
