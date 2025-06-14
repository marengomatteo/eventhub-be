package com.eventhub.event_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.event_service.dto.EventRequest;
import com.eventhub.event_service.service.EventService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventController {
    
    private final EventService eventService;

    @PostMapping("/create")
    public void createEvent(@RequestBody EventRequest request){
        EventRequest er = new EventRequest(request.getEventName(), request.getDate(),
                request.getTime(), request.getLocation(), request.getDescription(),
                request.getMaxPartecipants(), request.getEventType());
        eventService.createEvent(er);
    }

    @PostMapping("/edit")
    public void editEvent(@RequestBody EventRequest request) {
       EventRequest er = new EventRequest(request.getEventName(), request.getDate(),
                request.getTime(), request.getLocation(), request.getDescription(),
                request.getMaxPartecipants(), request.getEventType());
        eventService.editEvnt(er); 
    }

    @PostMapping("/delete")
    public void deleteEvent(@RequestBody String eventName) {
        eventService.deleteEvent(eventName);
    }
}
