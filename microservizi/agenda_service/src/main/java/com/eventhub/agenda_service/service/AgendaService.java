package com.eventhub.agenda_service.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.eventhub.agenda_service.dto.AgendaRequest;
import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.entities.Agenda;
import com.eventhub.agenda_service.mapper.AgendaMapper;
import com.eventhub.agenda_service.repositories.AgendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final AgendaMapper agendaMapper;

    public List<AgendaResponse> getAllAgendas() {
        try {
            List<Agenda> agenda = agendaRepository.findAll();
            return agenda.stream().map(ag -> agendaMapper.convert(ag)).toList();
        } catch (Exception e) {
            log.error("Error retrieving all agenda: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve agenda for event", e);
        }
    }

    public AgendaResponse getAgendaByEvent(String id) {
        try {
            Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> {
                log.error("Agenda with event id {} not found", id);
                throw new RuntimeException("Agenda not found");
            });
            AgendaResponse agendaResponse = agendaMapper.convert(agenda);

            return agendaResponse;
        } catch (Exception e) {
            log.error("Error retrieving agenda for event with id {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to retrieve agenda for event", e);
        }
    }

    public String newAgenda(AgendaRequest request) {
        try {
            Agenda agenda = agendaMapper.parse(request);

            Agenda agendaSaved = agendaRepository.save(agenda);
            return agendaSaved.getId().toString();

        } catch (DataAccessException e) {
            log.error("Error creating new event: ", e);
            throw new RuntimeException("Failed to create new event", e);
        }
    }

    public String updateAgenda(String id, AgendaRequest request) {
        try {

            Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> {
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

    public void deleteAgenda(String id) {
        try {
            Agenda a = agendaRepository.findById(id).orElseThrow(() -> {
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
