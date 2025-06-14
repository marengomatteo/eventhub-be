package com.eventhub.event_service.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.event_service.dto.EventRequest;
import com.eventhub.event_service.entities.Event;
import com.eventhub.event_service.repositories.EventRepository;
import com.eventhub.event_service.service.EventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping("")
    public void createEvent(@RequestBody EventRequest r) {
        eventService.newEvent(r);
    }

    @PutMapping("")
    public void updateEvent(@RequestBody EventRequest r) {    
        eventService.updateEvent(r);
    }

    @DeleteMapping("")
    public void deleteEvent(@RequestBody EventRequest r) {
        eventService.deleteEvent(r);
    }
}
