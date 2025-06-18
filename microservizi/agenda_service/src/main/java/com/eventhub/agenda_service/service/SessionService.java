package com.eventhub.agenda_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
                        return new RuntimeException("Agenda not found with id: " + agendaId);
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
            throw new RuntimeException("Database error occurred while adding session", e);
        }
    }

    public void updateSession(String agendaId, String sessionId, SessionRequest request) {
        try {
            Agenda agenda = agendaRepository.findById(agendaId)
                    .orElseThrow(() -> {
                        log.error("Agenda not found with id: {}", agendaId);
                        return new IllegalArgumentException("Agenda not found with id: " + agendaId);
                    });

            // Trova la sessione da aggiornare
            Sessione existingSession = agenda.getSessions().stream()
                    .filter(s -> s.getId().equals(sessionId))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("Session not found with id: {} in agenda: {}", sessionId, agendaId);
                        return new IllegalArgumentException("Session not found with id: " + sessionId);
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
            throw new RuntimeException("Database error occurred while updating session", e);
        }
    }

    public void deleteSession(String agendaId, String sessionId) {
        try {
            Agenda agenda = agendaRepository.findById(agendaId)
                    .orElseThrow(() -> {
                        log.error("Agenda not found with id: {}", agendaId);
                        return new IllegalArgumentException("Agenda not found with id: " + agendaId);
                    });

            boolean removed = agenda.getSessions().removeIf(s -> s.getId().equals(sessionId));

            if (!removed) {
                log.error("Session not found with id: {} in agenda: {}", sessionId, agendaId);
                throw new IllegalArgumentException("Session not found with id: " + sessionId);
            }

            agendaRepository.save(agenda);
            log.info("Deleted session {} from agenda {}", sessionId, agendaId);

        } catch (DataAccessException e) {
            log.error("Database error deleting session {} from agenda {}", sessionId, agendaId, e);
            throw new RuntimeException("Database error occurred while deleting session", e);
        }
    }

    private void validateSessionDateWithAgenda(SessionRequest request, LocalDate agendaDay) {
        if (request.getStartTime() != null &&
                !request.getStartTime().toLocalDate().equals(agendaDay)) {
            throw new IllegalArgumentException(
                    "Session start time must be on the same day as agenda: " + agendaDay);
        }

        if (request.getEndTime() != null &&
                !request.getEndTime().toLocalDate().equals(agendaDay)) {
            throw new IllegalArgumentException(
                    "Session end time must be on the same day as agenda: " + agendaDay);
        }
    }

    private void validateSessionOverlaps(SessionRequest request, List<Sessione> existingSessions) {
        LocalDateTime newStart = request.getStartTime();
        LocalDateTime newEnd = request.getEndTime();

        for (Sessione existing : existingSessions) {
            if (existing.getStartTime() != null && existing.getEndTime() != null) {
                if (newStart.isBefore(existing.getEndTime()) &&
                        newEnd.isAfter(existing.getStartTime())) {
                    throw new IllegalArgumentException(
                            "Session overlaps with existing session: " + existing.getTitle() +
                                    " (" + existing.getStartTime() + " - " + existing.getEndTime() + ")");
                }
            }
        }
    }
}
