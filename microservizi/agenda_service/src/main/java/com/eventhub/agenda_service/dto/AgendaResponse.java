package com.eventhub.agenda_service.dto;

import java.time.LocalDate;
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
    private LocalDate day;
    private List<Sessione> sessionsList;
}
