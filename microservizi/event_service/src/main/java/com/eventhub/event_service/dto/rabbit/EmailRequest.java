package com.eventhub.event_service.dto.rabbit;

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
public class EmailRequest implements Serializable {

    @JsonProperty("to")
    private String to;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("source")
    private String source;

    @JsonProperty("eventType")
    private Optional<String> eventType;

    @JsonProperty("templateData")
    private Map<String, Object> templateData;

}