package com.eventhub.utenti_service.service;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.utenti_service.dto.login.LoginRequest;
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

    private final AuthenticationManager authenticationManager;
    private final PasswordHasher passwordHasher;

    @Transactional
    public boolean userExists(String email) {
        return utenteRepository.findUserByEmail(email).isPresent();
    }

    @Transactional
    public Utente loadUserByUsername(String email) {
        return utenteRepository.findUserByEmail(email).orElseThrow(() -> {
            throw new BadCredentialsException("Email o password invalide.");
        });
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

    @Transactional
    public Utente login(LoginRequest loginRequest) {
        try {
            Utente utente = loadUserByUsername(loginRequest.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest
                                    .getEmail(),
                            loginRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                log.info("{} Utente autenticato correttamente: {}", "login", loginRequest.getEmail());
                return utente;
            } else {
                log.error("{} Fallita autenticazione per utente con email {}", "login", loginRequest.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "RESPONSE_STATUS.UNAUTHORIZED_CREDENTIAL");
            }

        } catch (BadCredentialsException bce) {
            log.error("{} Errore durante l'autenticazione dell'utente {}", "login", loginRequest.getEmail());
            log.error("{}: {}", "login", bce);
            throw new BadCredentialsException(
                    "Login fallito. Email o password invalide");
        } catch (DataAccessException e) {
            log.error("{} Errore durante l'autenticazione dell'utente {}", "login", loginRequest.getEmail());
            log.error("{}: {}", "login", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "RESPONSE_STATUS.INTERNAL_SERVER_ERROR");
        }
    }

}
