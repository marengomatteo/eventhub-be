package com.eventhub.utenti_service.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.utenti_service.configuration.RabbitMQConfig;
import com.eventhub.utenti_service.dto.login.GoogleUserInfo;
import com.eventhub.utenti_service.dto.login.LoginRequest;
import com.eventhub.utenti_service.dto.login.UserDataResponse;
import com.eventhub.utenti_service.dto.rabbit.EmailRequest;
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
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final PasswordHasher passwordHasher;

    private final RabbitTemplate rabbitTemplate;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

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
                        "Si è verificato un errore. Verifica i dati inseriti e riprova");
            }

            Utente u = utenteMapper.parse(request);

            u.setRole(ERole.USER);
            u.setPassword(null);

            if (request.getProvider().equals(EProvider.STATIC)) {
                log.warn("{}: Utente con email {} provider: STATIC salvo password", "signupUser",
                        request.getEmail());
                if (request.getPassword() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Si è verificato un errore. Verifica i dati inseriti e riprova");
                } else {
                    u.setPassword(passwordHasher.hashPassword(request.getPassword()));
                }
            }

            Utente savedUtente = utenteRepository.save(u);

            log.debug("Creazione del DTO per il messaggio");

            String subject = "Ciao " + request.getName() + " " + request.getSurname();
            String body = "La registrazione è stata completata con successo!";

            EmailRequest er = new EmailRequest(request.getEmail(), subject, body, "USER", Optional.empty(),
                    new HashMap<>());

            log.debug("Pubblicazione dell'evento su RabbitMQ");
            // Pubblicazione dell'evento su RabbitMQ
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    er);

            return savedUtente.getId().toString();

        } catch (DataAccessException e) {
            log.error("{} Errore durante il salvataggio dell'utente con email {}", "register", request.getEmail());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Errore generico del server");
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
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non corrette");
            }

        } catch (BadCredentialsException bce) {
            log.error("{} Errore durante l'autenticazione dell'utente {}", "login", loginRequest.getEmail());
            log.error("{}: {}", "login", bce);
            throw new BadCredentialsException(
                    "Login fallito. Email o password invalide");
        } catch (DataAccessException e) {
            log.error("{} Errore durante l'autenticazione dell'utente {}", "login", loginRequest.getEmail());
            log.error("{}: {}", "login", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore generico del server");
        }
    }

    @Transactional
    public Utente processGoogleUser(GoogleUserInfo userInfo) {
        try {
            if (!userExists(userInfo.getEmail())) {
                Utente u = createGoogleUser(userInfo);
                return utenteRepository.save(u);
            } else {
                return loadUserByUsername(userInfo.getEmail());
            }
        } catch (DataAccessException e) {
            log.error("{} Errore durante l'autenticazione dell'utente {}", "login", userInfo.getEmail());
            log.error("{}: {}", "login", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore generico del server");

        }

    }

    private Utente createGoogleUser(GoogleUserInfo userInfo) {
        Utente u = new Utente();
        u.setEmail(userInfo.getEmail());
        String[] userInfoName = userInfo.getName().split(" ");
        u.setName(userInfoName[0]);
        u.setSurname(userInfoName[1]);
        u.setPassword(null);
        u.setRole(ERole.USER);
        u.setProvider(EProvider.GOOGLE);

        return u;
    }

    public boolean authenticationGoogleUser(Utente user) {
        try {
            List<GrantedAuthority> authorities = Collections
                    .singletonList(new SimpleGrantedAuthority(user.getRole().toString()));

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(),
                    authorities);

            if (authentication.isAuthenticated()) {
                log.info("{} Utente autenticato correttamente: {}", "login", user.getEmail());
                return true;
            } else {
                log.error("{} Fallita autenticazione per utente con email {}", "login", user.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenziali non corrette");
            }

        } catch (BadCredentialsException bce) {
            log.error("{} Errore durante l'autenticazione dell'utente {}", "login", user.getEmail());
            log.error("{}: ", "login", bce);
            throw new BadCredentialsException(
                    "Login fallito. Email o password invalide");
        } catch (DataAccessException e) {
            log.error("{} Errore durante l'autenticazione dell'utente {}", "login", user.getEmail());
            log.error("{}: {}", "login", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore generico del server");
        }
    }

    public UserDataResponse getUserData(String token) {
        try {

            String email = jwtService.extractUserId(token);

            Utente utente = loadUserByUsername(email);

            return utenteMapper.convert(utente);

        } catch (BadCredentialsException bce) {
            log.error("{} Errore durante la get dati utente", "getUserData");
            throw new BadCredentialsException(
                    "Login fallito. Email o password invalide");
        } catch (Exception e) {
            System.err.println("Errore nel parsing del JWT: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore generico del server");
        }
    }

}
