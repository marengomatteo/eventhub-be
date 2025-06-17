package com.eventhub.agenda_service.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse {

    private UUID id;
    private String eventId;
    // private List<> sessionsList;
}
