package com.eventhub.utenti_service.dto.signup;

import org.springframework.validation.annotation.Validated;

import com.eventhub.utenti_service.entities.EProvider;
import com.eventhub.utenti_service.utils.Password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Validated
public class SignUpRequest {

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    @NotBlank(message = "Il nome è obbligatorio")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Il nome deve contenere solo lettere")
    private String name;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Il cognome deve contenere solo lettere")
    private String surname;

    @Password(required = false)
    private String password;

    @NotNull(message = "Il provider è obbligatorio")
    private EProvider provider = EProvider.STATIC;

    private String externalId;

}