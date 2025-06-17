package com.eventhub.agenda_service.dto;

import java.util.List;

import com.eventhub.agenda_service.entities.Session;

import lombok.Data;

@Data
public class AgendaResponse {

    private String id;
    private String eventName;
    private List<Session> sessionsList; 

}
