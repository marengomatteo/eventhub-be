package com.eventhub.event_service.service;

import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.eventhub.event_service.dto.EventRequest;
import com.eventhub.event_service.entities.Event;
import com.eventhub.event_service.repositories.EventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    public void newEvent(EventRequest request) {
        try {
        Event e = new Event();
        e.setEventName(request.getEventName());
        e.setStartDate(request.getStartDate());
        e.setEndDate(request.getEndDate());
        e.setTime(request.getTime());
        e.setLocation(request.getLocation());
        e.setMaxPartecipants(request.getMaxPartecipants());
        e.setEventType(request.getEventType());
        e.setDescription(request.getDescription());
        Event esaved = eventRepository.save(e);
        log.info(esaved.getId());

        } catch (Exception e) { //TODO: errore troppo generico
            log.error("Error creating new event: ", e);
            throw new RuntimeException("Failed to create new event", e);
        }
    }

    public void updateEvent(EventRequest request) {
        try {
        Event e = eventRepository.findById(request.getId())
        e.setEventName(request.getEventName());
        e.setStartDate(request.getStartDate());
        e.setEndDate(request.getEndDate());
        e.setTime(request.getTime());
        e.setLocation(request.getLocation());
        e.setMaxPartecipants(request.getMaxPartecipants());
        e.setEventType(request.getEventType());
        e.setDescription(request.getDescription());
        Event esaved = eventRepository.save(e);
        log.info(esaved.getId());

        } catch (Exception e) {
            log.error("Error updating event {}: ", request.getId(), e);
            throw new RuntimeException("Failed to update event", e);
        }
    }

    public void deleteEvent(EventRequest request) {
        try {
            eventRepository.deleteById(request.getId());
        } catch (Exception e) { 
            log.error("Error deleting event {}: ", request.getId(), e);
            throw new RuntimeException("Failed to delete event", e);
        }
    }
}
