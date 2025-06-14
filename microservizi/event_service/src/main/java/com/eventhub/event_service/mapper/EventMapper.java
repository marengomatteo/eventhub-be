package com.eventhub.event_service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.eventhub.event_service.dto.EventRequest;
import com.eventhub.event_service.dto.EventResponse;
import com.eventhub.event_service.entities.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event parse(EventRequest eventRequest);

    EventResponse convert(Event event);

    List<EventResponse> convert(List<Event> event);
}
