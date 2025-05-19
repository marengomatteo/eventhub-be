package com.eventhub.mail_service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserEventDto implements Serializable {
    @JsonProperty("user")
    private UserMessageDto user;
    
    @JsonProperty("eventType")
    private String eventType;
}
