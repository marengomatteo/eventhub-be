package com.eventhub.agenda_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.agenda_service.dto.SessionRequest;
import com.eventhub.agenda_service.entities.Agenda;
import com.eventhub.agenda_service.entities.Sessione;
import com.eventhub.agenda_service.mapper.AgendaMapper;
import com.eventhub.agenda_service.repositories.AgendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final AgendaRepository agendaRepository;
    private final AgendaMapper agendaMapper;

    public String addSessionToAgenda(String agendaId, SessionRequest request) {
        try {
            Agenda agenda = agendaRepository.findById(agendaId)
                    .orElseThrow(() -> {
                        log.error("Agenda not found with id: {}", agendaId);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Agenda non trovata" );
                    });

            validateSessionDateWithAgenda(request, agenda.getDay());

            validateSessionOverlaps(request, agenda.getSessions());

            Sessione newSession = agendaMapper.mapSession(request);
            agenda.getSessions().add(newSession);

            agendaRepository.save(agenda);
            log.info("Added session '{}' to agenda {}", newSession.getTitle(), agendaId);

            return newSession.getId();
        } catch (DataAccessException e) {
            log.error("Database error adding session to agenda {}", agendaId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

    public void updateSession(String agendaId, String sessionId, SessionRequest request) {
        try {
            Agenda agenda = agendaRepository.findById(agendaId)
                    .orElseThrow(() -> {
                        log.error("Agenda not found with id: {}", agendaId);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Agenda non trovata");
                    });

            // Trova la sessione da aggiornare
            Sessione existingSession = agenda.getSessions().stream()
                    .filter(s -> s.getId().equals(sessionId))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("Session not found with id: {} in agenda: {}", sessionId, agendaId);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Session dell'agenda non trovata");
                    });

            validateSessionDateWithAgenda(request, agenda.getDay());

            // Aggiorna i campi della sessione esistente
            existingSession.setSpeaker(request.getSpeaker());
            existingSession.setTitle(request.getTitle());
            existingSession.setLocation(request.getLocation());
            existingSession.setDescription(request.getDescription());
            existingSession.setStartTime(request.getStartTime());
            existingSession.setEndTime(request.getEndTime());

            agendaRepository.save(agenda);
            log.info("Updated session '{}' in agenda {}", existingSession.getTitle(), agendaId);

        } catch (DataAccessException e) {
            log.error("Database error updating session {} in agenda {}", sessionId, agendaId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

    public void deleteSession(String agendaId, String sessionId) {
        try {
            Agenda agenda = agendaRepository.findById(agendaId)
                    .orElseThrow(() -> {
                        log.error("Agenda not found with id: {}", agendaId);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Agenda non trovata");
                    });

            boolean removed = agenda.getSessions().removeIf(s -> s.getId().equals(sessionId));

            if (!removed) {
                log.error("Session not found with id: {} in agenda: {}", sessionId, agendaId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Session non trovata nell'agenda");
            }

            agendaRepository.save(agenda);
            log.info("Deleted session {} from agenda {}", sessionId, agendaId);

        } catch (DataAccessException e) {
            log.error("Database error deleting session {} from agenda {}", sessionId, agendaId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Errore generico del server");
        }
    }

    private void validateSessionDateWithAgenda(SessionRequest request, LocalDate agendaDay) {
        if (request.getStartTime() != null &&
                !request.getStartTime().toLocalDate().equals(agendaDay)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tempo inizio sessione deve essere nello stesso giorno dell'agenda");
        }

        if (request.getEndTime() != null &&
                !request.getEndTime().toLocalDate().equals(agendaDay)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tempo fine sessione deve essere nello stesso giorno dell'agenda");
        }
    }

    private void validateSessionOverlaps(SessionRequest request, List<Sessione> existingSessions) {
        LocalDateTime newStart = request.getStartTime();
        LocalDateTime newEnd = request.getEndTime();

        for (Sessione existing : existingSessions) {
            if (existing.getStartTime() != null && existing.getEndTime() != null) {
                if (newStart.isBefore(existing.getEndTime()) &&
                        newEnd.isAfter(existing.getStartTime())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La sessione si sovrappone con un'altra sessione: " + existing.getTitle() +
                                    " (" + existing.getStartTime() + " - " + existing.getEndTime() + ")");
                }
            }
        }
    }
}
