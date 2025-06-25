package com.eventhub.event_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.agenda_service.proto.CreateAgendaRequest;
import com.eventhub.agenda_service.proto.CreateAgendaResponse;
import com.eventhub.event_service.config.RabbitMQConfig;
import com.eventhub.event_service.dto.EventDetailResponse;
import com.eventhub.event_service.dto.EventRequest;
import com.eventhub.event_service.dto.EventResponse;
import com.eventhub.event_service.dto.rabbit.EmailRequest;
import com.eventhub.event_service.entities.Event;
import com.eventhub.event_service.entities.Participant;
import com.eventhub.event_service.mapper.EventMapper;
import com.eventhub.event_service.repositories.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final AgendaClientService greeterClientService;

    private final RabbitTemplate rabbitTemplate;

    public List<EventResponse> getAllEvents() {
        try {
            return eventMapper.convert(eventRepository.findAll());
        } catch (DataAccessException dae) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

    public List<EventDetailResponse> getUserEvents(String userId) {
        try {
            return eventMapper.convertToDetailResponse(eventRepository.findByUserId(userId));
        } catch (DataAccessException dae) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

    public String newEvent(EventRequest request) {
        try {
            Event e = eventMapper.parse(request);
            e.setPartecipantsList(new ArrayList<>());
            Event esaved = eventRepository.save(e);

            // chiamata grpc ad agenda
            CreateAgendaRequest createAgendaRequest = CreateAgendaRequest
                    .newBuilder()
                    .setDay(request.getStartDate())
                    .setEventId(esaved.getId())
                    .setEventName(request.getEventName())
                    .build();

            CreateAgendaResponse response = greeterClientService.creaAgenda(createAgendaRequest);
            if (!response.getSuccess()) {
                eventRepository.delete(esaved);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Agenda creation failed");
            }
            return response.getAgendaId();
        } catch (DataAccessException e) {
            log.error("Error creating new event: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

    public void updateEvent(String id, EventRequest request) {
        try {
            Event event = eventRepository.findById(id).orElseThrow(() -> {
                log.error("Event with id {} not found for update", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Evento non trovato per l'aggiornamento");
            });

            event.setEventName(request.getEventName());
            event.setStartDate(request.getStartDate());
            event.setEndDate(request.getEndDate());
            event.setTime(request.getTime());
            event.setLocation(request.getLocation());
            event.setMaxPartecipants(request.getMaxPartecipants());
            event.setEventType(request.getEventType());
            event.setDescription(request.getDescription());

            Event esaved = eventRepository.save(event);
            log.info(esaved.getId());

        } catch (Exception e) {
            log.error("Error updating event {}: ", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

    public void deleteEvent(String id) {
        try {
            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Evento non trovato per l'eliminazione"));
            List<Participant> partecipantsList = event.getPartecipantsList();
            String eventName = event.getEventName();

            if (partecipantsList != null && !partecipantsList.isEmpty()) {

                for (int i = 0; i < partecipantsList.size(); i++) {
                    Participant p = partecipantsList.get(i);
                    String subject = "Evento cancellato";
                    String body = "L'evento {} a cui ti sei iscritto è stato cancellato."
                            .formatted(eventName);

                    EmailRequest er = new EmailRequest(p.getEmail(), subject, body, "USER", Optional.empty(),
                            new HashMap<>());
                    rabbitTemplate.convertAndSend(
                            RabbitMQConfig.EXCHANGE_NAME,
                            RabbitMQConfig.ROUTING_KEY,
                            er);
                }
            }
            eventRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting event {}: ", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
        }
    }

}
