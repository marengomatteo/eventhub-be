package com.eventhub.utenti_service.dto.login;

import lombok.Data;

@Data
public class GoogleUserInfo {
    private String sub;
    private String email;
    private String name;
}
