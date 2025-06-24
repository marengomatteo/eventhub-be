package com.eventhub.event_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {

    private String id;
    private String eventName;
    private String startDate;
    private String endDate;
    private String time;
    private String location;
    private String description;
    private int maxPartecipants;
    private String eventType;
    private String userId;

}
