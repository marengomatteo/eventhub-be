package com.eventhub.utenti_service.service;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.utenti_service.dto.signup.SignUpRequest;
import com.eventhub.utenti_service.entities.EProvider;
import com.eventhub.utenti_service.entities.ERole;
import com.eventhub.utenti_service.entities.Utente;
import com.eventhub.utenti_service.mapper.UtenteMapper;
import com.eventhub.utenti_service.repositories.UtenteRepository;
import com.eventhub.utenti_service.utils.PasswordHasher;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtenteRepository utenteRepository;
    private final UtenteMapper utenteMapper;

    private final PasswordHasher passwordHasher;

    @Transactional
    public boolean userExists(String email) {
        return utenteRepository.findUserByEmail(email).isPresent();
    }

    @Transactional
    public String signupUser(SignUpRequest request) {
        try {
            log.info("{}: Tentativo di creazione utente con email {}", "signupUser", request.getEmail());

            if (userExists(request.getEmail())) {
                log.error("{}: Utente già presente a DB con questa email", "signupUser");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "RESPONSE_STATUS.BAD_REQUEST");
            }

            Utente u = utenteMapper.parse(request);

            u.setRole(ERole.USER);
            u.setPassword(null);

            if (request.getProvider().equals(EProvider.STATIC)) {
                log.warn("{}: Utente con email {} provider: STATIC salvo password", "signupUser",
                        request.getEmail());
                if (request.getPassword() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "RESPONSE_STATUS.BAD_REQUEST");
                } else {
                    u.setPassword(passwordHasher.hashPassword(request.getPassword()));
                }
            }

            Utente savedUtente = utenteRepository.save(u);

            return savedUtente.getId().toString();

        } catch (DataAccessException e) {
            log.error("{} Errore durante il salvataggio dell'utente con email {}", "register", request.getEmail());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "RESPONSE_STATUS.INTERNAL_SERVER_ERROR");
        }
    }

}
