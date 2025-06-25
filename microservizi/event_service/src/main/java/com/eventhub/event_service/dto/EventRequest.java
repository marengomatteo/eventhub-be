package com.eventhub.event_service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotNull(message = "Nome evento obbligatorio")
    private String eventName;

    @NotNull(message = "Ora inizio obbligatoria")
    private LocalDateTime startTime;

    @NotNull(message = "Ora fine obbligatoria")
    private LocalDateTime endTime;

    @NotNull(message = "Luogo obbligatorio")
    private String location;

    @NotNull(message = "Descrizione obbligatoria")
    private String description;

    @NotNull(message = "Numero massimo partecipanti obbligatorio")
    private int maxPartecipants;

    @NotNull(message = "Tipo evento obbligatorio")
    private String eventType;

    @NotNull(message = "Id utente obbligatorio")
    private String userId;

}
