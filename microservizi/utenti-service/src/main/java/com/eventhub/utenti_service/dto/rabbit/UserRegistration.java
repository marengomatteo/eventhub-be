package com.eventhub.utenti_service.dto.rabbit;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistration implements Serializable {
    @JsonProperty("id")
    private UUID id;
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;
    
    @JsonProperty("email")
    private String email;
}
