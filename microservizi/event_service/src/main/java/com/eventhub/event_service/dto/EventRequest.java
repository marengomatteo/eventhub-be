package com.eventhub.event_service.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    private String eventName;

    private String date;

    private String time;

    private String location;

    private String description;

    private int maxPartecipants;

    private Optional<String> eventType;

}
