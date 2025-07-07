package com.eventhub.event_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.event_service.dto.EventDetailResponse;
import com.eventhub.event_service.dto.EventRequest;
import com.eventhub.event_service.dto.EventResponse;
import com.eventhub.event_service.dto.MessageResponse;
import com.eventhub.event_service.entities.Participant;
import com.eventhub.event_service.service.EventService;
import com.eventhub.event_service.service.TicketClientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final TicketClientService ticketClientService;

    @GetMapping("/list")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{userId}/list")
    public ResponseEntity<List<EventDetailResponse>> getUserEvents(@PathVariable("userId") String id) {
        List<EventDetailResponse> events = eventService.getUserEvents(id);
        return ResponseEntity.ok(events);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createEvent(@RequestBody EventRequest r) {
        String id = eventService.newEvent(r);
        return ResponseEntity.ok(new MessageResponse(id));
    }

    @PostMapping("/{id}/registration")
    public ResponseEntity<MessageResponse> addParticipant(@PathVariable("id") String id,
            @RequestBody Participant participant) {
        ticketClientService.addParticipant(id, participant);
        return ResponseEntity.ok(new MessageResponse("Participant added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateEvent(
            @PathVariable("id") String id,
            @RequestBody EventRequest r) {
        eventService.updateEvent(id, r);
        return ResponseEntity.ok(new MessageResponse("Event updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteEvent(@PathVariable("id") String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new MessageResponse("Event deleted successfully"));
    }

}
