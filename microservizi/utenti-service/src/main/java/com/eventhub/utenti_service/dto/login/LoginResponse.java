package com.eventhub.utenti_service.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private UserDataResponse userDataResponse;
    private String accessToken;
    private String refreshToken;
}
