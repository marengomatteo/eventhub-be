package com.eventhub.agenda_service.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaRequest {

    @NotBlank
    private String eventId;

    @NotBlank
    private String eventName;

    private LocalDate day;

    private List<SessionRequest> sessionsList;

}