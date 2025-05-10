package com.eventhub.utenti_service.mapper;

import org.mapstruct.Mapper;

import com.eventhub.utenti_service.dto.signup.SignUpRequest;
import com.eventhub.utenti_service.entities.Utente;

@Mapper(componentModel = "spring")
public interface UtenteMapper {

    Utente parse(SignUpRequest signUpRequest);

}
