package com.eventhub.mail_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserMessageDto implements Serializable {
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
}
