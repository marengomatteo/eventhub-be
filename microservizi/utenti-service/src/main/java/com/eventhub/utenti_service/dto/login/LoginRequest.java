package com.eventhub.utenti_service.dto.login;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
