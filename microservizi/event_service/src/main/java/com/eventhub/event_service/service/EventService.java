package com.eventhub.event_service.service;

import org.springframework.stereotype.Service;

import com.eventhub.event_service.dto.EventRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    
    public void createEvent(EventRequest eventRequest) {
        log.info("Creating event: {}", eventRequest.getEventName());
    }

    public void editEvnt(EventRequest eventRequest) {
        log.info("Editing event: {}", eventRequest.getEventName());
    }

    public void deleteEvent(String eventName) {
        log.info("Deleting event: {}", eventName);
    }
}
