package com.eventhub.agenda_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.dto.MessageResponse;
import com.eventhub.agenda_service.dto.SessionRequest;
import com.eventhub.agenda_service.service.AgendaService;
import com.eventhub.agenda_service.service.SessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
@Slf4j
public class AgendaController {

    private final AgendaService agendaService;
    private final SessionService sessionService;

    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponse> getAgendaByEvent(@PathVariable("id") String eventId) {
        AgendaResponse agenda = agendaService.getAgendaByEvent(eventId);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AgendaResponse>> getAllAgenda() {
        List<AgendaResponse> agenda = agendaService.getAllAgendas();
        return ResponseEntity.ok(agenda);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteAgenda(@PathVariable("id") String id) {
        agendaService.deleteAgenda(id);
        return ResponseEntity.ok(new MessageResponse("Agenda deleted successfully"));
    }

    @PostMapping("/{agendaId}/sessions")
    public ResponseEntity<MessageResponse> addSessionToAgenda(
            @PathVariable String agendaId,
            @Valid @RequestBody SessionRequest request) {

        String sessionId = sessionService.addSessionToAgenda(agendaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(sessionId));

    }

    @DeleteMapping("/{agendaId}/sessions/{sessionId}")
    public ResponseEntity<MessageResponse> deleteSession(
            @PathVariable String agendaId,
            @PathVariable String sessionId) {

        sessionService.deleteSession(agendaId, sessionId);
        return ResponseEntity.ok(new MessageResponse("Session deleted successfully"));

    }
}
