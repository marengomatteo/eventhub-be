package com.eventhub.event_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.event_service.entities.Event;
import com.eventhub.event_service.repositories.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping("")
    public void createEvent() {
        Event e1 = new Event();
        e1.setEventName("Concerto Rock");
        e1.setStartDate("2024-12-15");
        e1.setEndDate("2024-12-15");
        e1.setTime("20:00");
        e1.setLocation("Arena di Verona");
        e1.setMaxPartecipants("5000");
        e1.setEventType("concerto");
        e1.setDescription("Descrizione");
        Event esaved = eventRepository.save(e1);
        log.info(esaved.getId());

    }

}
