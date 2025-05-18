package com.eventhub.utenti_service.dto.rabbit;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEventRegistration implements Serializable{
    
    @JsonProperty("user")
    private UserRegistration user;

    @JsonProperty("eventType")
    private String eventType;
}
