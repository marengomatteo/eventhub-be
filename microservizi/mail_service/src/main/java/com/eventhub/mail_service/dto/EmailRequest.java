package com.eventhub.mail_service.dto;

import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
    private String source;
    private Optional<String> eventType;
    private Map<String, Object> templateData;

}
