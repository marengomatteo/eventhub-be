package com.eventhub.utenti_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eventhub.utenti_service.entities.RefreshToken;
import com.eventhub.utenti_service.entities.Utente;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(UUID userid);

    @Modifying
    int deleteByUser(Utente user);

}
