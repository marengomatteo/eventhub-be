package com.eventhub.agenda_service.dto;

import lombok.Data;

@Data
public class SessionResponse {

    private String id;
    private String speaker;
    private String title;
    private String location;
    private String description;
    private String startTime;
    private String endTime;
    
}
