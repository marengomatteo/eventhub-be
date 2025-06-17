package com.eventhub.agenda_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.eventhub.agenda_service.dto.AgendaRequest;
import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.entities.Agenda;
import com.eventhub.agenda_service.repositories.AgendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendaService {
    
    private final AgendaRepository agendaRepository;
    private final AgendaMapper agendaMapper;



    public AgendaResponse getAgendaByEvent(String id) {
        try{
            AgendaResponse response = agendaRepository.findById(id).orElseThrow(() -> {
                log.error("Agenda with event id {} not found", id);
                throw new RuntimeException("Agenda not found");
            });     
        } catch (Exception e) {
            log.error("Error retrieving agenda for event with id {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to retrieve agenda for event", e);
        }
    }

    public void newAgenda(AgendaRequest request) {
        try {
            Agenda e = agendaMapper.parse(request);
            e.setPartecipantsList(new ArrayList<>());
            Event esaved = eventRepository.save(e);

            return esaved.getId();
        } catch (DataAccessException e) {
            log.error("Error creating new event: ", e);
            throw new RuntimeException("Failed to create new event", e);
        }
    }

    public void updateAgenda(String id) {
        // Logic to update the agenda
    }

    public void deleteAgenda(String id) {
        // Logic to delete the agenda
    }
}

