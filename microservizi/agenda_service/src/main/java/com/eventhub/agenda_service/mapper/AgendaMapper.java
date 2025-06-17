package com.eventhub.agenda_service.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.eventhub.agenda_service.dto.AgendaResponse;
import com.eventhub.agenda_service.entities.Agenda;

@Mapper(componentModel = "spring")
public interface AgendaMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    AgendaResponse convert(Agenda agenda);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

}
