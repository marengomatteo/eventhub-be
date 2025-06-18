package com.eventhub.agenda_service.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SessionRequest {

    @NotBlank(message = "Speaker obbligatorio")
    private String speaker;

    @NotBlank(message = "Titolo obbligatorio")
    private String title;

    private String location;

    private String description;

    @NotNull(message = "Ora inizio obbligatoria")
    private LocalDateTime startTime;

    @NotNull(message = "Ora fine obbligatoria")
    private LocalDateTime endTime;

    @AssertTrue(message = "L'ora di fine deve essere successiva all'ora di inizio")
    @JsonIgnore
    @Schema(hidden = true)
    public boolean isValidTimeRange() {
        return startTime == null || endTime == null || endTime.isAfter(startTime);
    }

}
