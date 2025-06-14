package com.eventhub.event_service.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest implements Serializable {

    @JsonProperty("eventName")
    private String eventName;

    @JsonProperty("date")
    private String date;

    @JsonProperty("time")
    private String time;

    @JsonProperty("location")
    private String location;

    @JsonProperty("description")
    private String description;

    @JsonProperty("maxPartecipants")
    private int maxPartecipants;

    @JsonProperty("eventType")
    private Optional<String> eventType;
    
}
