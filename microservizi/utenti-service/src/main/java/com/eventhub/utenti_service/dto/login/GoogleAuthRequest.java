package com.eventhub.utenti_service.dto.login;

import lombok.Data;

@Data
public class GoogleAuthRequest {
    private String code;
    private String scope;
    private String authuser;
    private String prompt;
}
