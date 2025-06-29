package com.eventhub.event_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.eventhub.event_service.entities.Participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailResponse {

    private String id;
    private String eventName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String description;
    private int maxPartecipants;
    private List<Participant> partecipantsList;
    private String eventType;
    private String userId;

}
