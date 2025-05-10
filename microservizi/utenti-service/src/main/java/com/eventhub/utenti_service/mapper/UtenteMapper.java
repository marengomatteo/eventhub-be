package com.eventhub.utenti_service.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.eventhub.utenti_service.dto.login.UserDataResponse;
import com.eventhub.utenti_service.dto.signup.SignUpRequest;
import com.eventhub.utenti_service.entities.Utente;

@Mapper(componentModel = "spring")
public interface UtenteMapper {

    Utente parse(SignUpRequest signUpRequest);

    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    UserDataResponse convert(Utente utente);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

}
