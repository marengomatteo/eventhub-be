package com.eventhub.event_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {

    private String id;
    private String eventImage;
    private String eventName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String description;
    private int maxPartecipants;
    private String eventType;
    private String userId;

}
