package com.eventhub.utenti_service.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.utenti_service.entities.RefreshToken;
import com.eventhub.utenti_service.entities.Utente;
import com.eventhub.utenti_service.repositories.RefreshTokenRepository;
import com.eventhub.utenti_service.repositories.UtenteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UtenteRepository utenteRepository;
    private final JwtService jwtService;

    @Transactional
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RefreshToken createRefreshToken(Utente user) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plus(jwtService.getRefreshTokenDuration(), ChronoUnit.SECONDS));
        refreshToken.setToken(generateToken());

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Transactional
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {

        refreshToken.setToken(generateToken());
        refreshToken.setExpiryDate(LocalDateTime.now().plus(jwtService.getRefreshTokenDuration(), ChronoUnit.SECONDS));

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);

            log.info("{}: Refresh token scaduto", "verifyExpiration");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "RESPONSE_STATUS.FORBIDDEN_TOKEN");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(UUID userId) {
        Utente user = utenteRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("{}: Eliminazione refresh token fallita.", "deleteByUserId");
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "RESPONSE_STATUS.INTERNAL_SERVER_ERROR");
                });
        return refreshTokenRepository.deleteByUser(user);
    }

}
