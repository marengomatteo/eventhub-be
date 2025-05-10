package com.eventhub.utenti_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventhub.utenti_service.entities.Utente;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    Optional<Utente> findUserByEmail(String email);
}
