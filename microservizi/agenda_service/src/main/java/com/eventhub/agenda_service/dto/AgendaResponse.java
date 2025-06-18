package com.eventhub.agenda_service.dto;

import java.util.List;

import com.eventhub.agenda_service.entities.Sessione;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse {

    private String id;
    private String eventId;
    private String eventName;
    private List<Sessione> sessionsList;
}
