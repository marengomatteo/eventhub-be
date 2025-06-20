package com.eventhub.event_service.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    
    private String timestamp;
    private int status;
    private String message;
    private Map<String, String> errors;
    private String path;
    
}