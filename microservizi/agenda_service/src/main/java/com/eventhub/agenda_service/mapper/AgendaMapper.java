package com.eventhub.agenda_service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.eventhub.agenda_service.dto.AgendaRequest;
import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.dto.SessionRequest;
import com.eventhub.agenda_service.dto.SessionResponse;
import com.eventhub.agenda_service.entities.Agenda;
import com.eventhub.agenda_service.entities.Sessione;

@Mapper(componentModel = "spring")
public interface AgendaMapper {

    @Mapping(source = "sessionsList", target = "sessions", qualifiedByName = "mapSessions")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Agenda parse(AgendaRequest request);

    @Named("mapSessions")
    default List<Sessione> mapSessions(List<SessionRequest> sessionRequests) {
        if (sessionRequests == null) {
            return new ArrayList<>();
        }

        return sessionRequests.stream()
                .map(this::mapSession)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    Sessione mapSession(SessionRequest sessionRequest);

    @Mapping(source = "sessions", target = "sessionsList")
    AgendaResponse convert(Agenda agenda);

    SessionResponse mapSessionToResponse(Sessione sessione);
}
