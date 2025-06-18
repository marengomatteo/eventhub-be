package com.eventhub.agenda_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.agenda_service.dto.AgendaRequest;
import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.dto.AgendaUpdateRequest;
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
    public ResponseEntity<AgendaResponse> getAgendaByEvent(@PathVariable("id") String id) {
        AgendaResponse agenda = agendaService.getAgendaByEvent(id);
        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AgendaResponse>> getAllAgenda() {
        List<AgendaResponse> agenda = agendaService.getAllAgendas();
        return ResponseEntity.ok(agenda);
    }

    @PostMapping("")
    public ResponseEntity<String> newAgenda(@Valid @RequestBody AgendaRequest agendaRequest) {
        String id = agendaService.newAgenda(agendaRequest);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAgenda(@PathVariable("id") String id,
            @Valid @RequestBody AgendaUpdateRequest agendaRequest) {
        String data = agendaService.updateAgenda(id, agendaRequest);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgenda(@PathVariable("id") String id) {
        agendaService.deleteAgenda(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{agendaId}/sessions")
    public ResponseEntity<String> addSessionToAgenda(
            @PathVariable String agendaId,
            @Valid @RequestBody SessionRequest request) {

        String sessionId = sessionService.addSessionToAgenda(agendaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionId);

    }

    @PutMapping("/{agendaId}/sessions/{sessionId}")
    public ResponseEntity<String> updateSession(
            @PathVariable String agendaId,
            @PathVariable String sessionId,
            @Valid @RequestBody SessionRequest request) {

        sessionService.updateSession(agendaId, sessionId, request);
        return ResponseEntity.ok("Session updated successfully");

    }

    @DeleteMapping("/{agendaId}/sessions/{sessionId}")
    public ResponseEntity<String> deleteSession(
            @PathVariable String agendaId,
            @PathVariable String sessionId) {

        sessionService.deleteSession(agendaId, sessionId);
        return ResponseEntity.ok("Session deleted successfully");

    }
}
