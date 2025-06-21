package com.eventhub.agenda_service.dto;

import com.eventhub.agenda_service.entities.Speaker;

import lombok.Data;

@Data
public class SessionResponse {

    private String id;
    private Speaker speaker;
    private String title;
    private String location;
    private String description;
    private String startTime;
    private String endTime;

}
