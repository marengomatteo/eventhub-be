package com.eventhub.agenda_service.controller;

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
import com.eventhub.agenda_service.service.AgendaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
@Slf4j
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponse> getAgendaByEvent(@PathVariable("id") String id) {
        AgendaResponse agenda = agendaService.getAgendaByEvent(id);
        return ResponseEntity.ok(agenda);
    }

    @PostMapping("")
    public ResponseEntity<String> newAgenda(@RequestBody AgendaRequest agendaRequest) {
        String id = agendaService.newAgenda(agendaRequest);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAgenda(@PathVariable("id") String id,
            @RequestBody AgendaRequest agendaRequest) {
        String data = agendaService.updateAgenda(id, agendaRequest);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgenda(@PathVariable("id") String id) {
        agendaService.deleteAgenda(id);
        return ResponseEntity.ok().build();
    }

}
