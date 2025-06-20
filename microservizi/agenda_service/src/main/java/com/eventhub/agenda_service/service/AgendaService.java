package com.eventhub.agenda_service.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.agenda_service.dto.AgendaRequest;
import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.dto.AgendaUpdateRequest;
import com.eventhub.agenda_service.entities.Agenda;
import com.eventhub.agenda_service.entities.Sessione;
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno al server");  
        }
    }

    public AgendaResponse getAgendaByEvent(String eventId) {
        try {
            Agenda agenda = agendaRepository.findByEventId(eventId).orElseThrow(() -> {
                log.error("Agenda with event id {} not found", eventId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Agenda non trovata per l'evento");
            });
            AgendaResponse agendaResponse = agendaMapper.convert(agenda);

            return agendaResponse;
        } catch (Exception e) {
            log.error("Error retrieving agenda for event with id {}: {}", eventId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno al server");  
        }
    }

    public String newAgenda(AgendaRequest request) {
        try {
            Agenda agenda = agendaMapper.parse(request);

            Agenda agendaSaved = agendaRepository.save(agenda);
            return agendaSaved.getId().toString();

        } catch (DataAccessException e) {
            log.error("Error creating new event: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno al server");  
        }
    }

    public String updateAgenda(String id, AgendaUpdateRequest request) {
        try {
            Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> {
                log.error("Agenda not found with id: {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Agenda non trovata");
            });

            LocalDate oldDay = agenda.getDay();
            LocalDate newDay = request.getDay();

            if (!oldDay.equals(newDay)) {
                updateSessionsDates(agenda.getSessions(), oldDay, newDay);
            }

            agenda.setDay(newDay);

            Agenda agendaSaved = agendaRepository.save(agenda);
            return agendaSaved.getId();

        } catch (DataAccessException e) {
            log.error("Error updating agenda with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno al server");  
        }
    }

    private void updateSessionsDates(List<Sessione> sessions, LocalDate oldDay, LocalDate newDay) {
        for (Sessione session : sessions) {
            if (session.getStartTime() != null) {
                LocalTime startTime = session.getStartTime().toLocalTime();
                session.setStartTime(newDay.atTime(startTime));
            }

            if (session.getEndTime() != null) {
                LocalTime endTime = session.getEndTime().toLocalTime();
                session.setEndTime(newDay.atTime(endTime));
            }
        }
    }

    public void deleteAgenda(String id) {
        try {
            Agenda a = agendaRepository.findById(id).orElseThrow(() -> {
                log.error("Agenda with id: {} not found for deletion", id);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno al server");  
            });
            agendaRepository.delete(a);

        } catch (DataAccessException e) {
            log.error("Error deleting agenda with id: {}", id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno al server");  
        }
    }
}
