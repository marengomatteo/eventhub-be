package com.eventhub.agenda_service.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.eventhub.agenda_service.entities.Session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse {

    private String id;
    private String eventName;
    private List<Session> sessionsList; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
