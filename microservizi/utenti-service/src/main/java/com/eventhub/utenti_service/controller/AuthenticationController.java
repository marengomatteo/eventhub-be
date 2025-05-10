package com.eventhub.utenti_service.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.utenti_service.dto.login.LoginRequest;
import com.eventhub.utenti_service.dto.login.UserDataResponse;
import com.eventhub.utenti_service.dto.signup.SignUpRequest;
import com.eventhub.utenti_service.entities.RefreshToken;
import com.eventhub.utenti_service.entities.Utente;
import com.eventhub.utenti_service.mapper.UtenteMapper;
import com.eventhub.utenti_service.service.AuthenticationService;
import com.eventhub.utenti_service.service.JwtService;
import com.eventhub.utenti_service.service.RefreshTokenService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private final UtenteMapper utenteMapper;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utente creato correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validazione dati errata o utente già presente a database", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Errore nel salvataggio dei dati", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody SignUpRequest user) {
        String id = authenticationService.signupUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse(id));

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utente autenticato correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDataResponse.class))),
            @ApiResponse(responseCode = "400", description = "Username o password non validi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Errore sul database", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signin")
    public ResponseEntity<UserDataResponse> login(@RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        try {
            Utente authenticatedUser = authenticationService.login(loginRequest);

            ResponseCookie accessToken = jwtService.generateJwtCookie(authenticatedUser);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);

            ResponseCookie jwtRefreshCookie = jwtService.generateRefreshJwtCookie(refreshToken.getToken());

            UserDataResponse response = utenteMapper.convert(authenticatedUser);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessToken.toString())
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(response);

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getReason());
        }
    }

}
