package com.eventhub.utenti_service.dto.login;

import com.eventhub.utenti_service.entities.EProvider;
import com.eventhub.utenti_service.entities.ERole;

import lombok.Data;

@Data
public class UserDataResponse {

    private String id;
    private String email;
    private String name;
    private String surname;
    private EProvider provider;
    private String externalId;
    private ERole role;

}
