package com.eventhub.utenti_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eventhub.utenti_service.dto.login.GoogleAuthRequest;
import com.eventhub.utenti_service.dto.login.GoogleTokenResponse;
import com.eventhub.utenti_service.dto.login.GoogleUserInfo;
import com.eventhub.utenti_service.dto.login.LoginRequest;
import com.eventhub.utenti_service.dto.login.LoginResponse;
import com.eventhub.utenti_service.dto.login.UserDataResponse;
import com.eventhub.utenti_service.dto.signup.SignUpRequest;
import com.eventhub.utenti_service.entities.RefreshToken;
import com.eventhub.utenti_service.entities.Utente;
import com.eventhub.utenti_service.mapper.UtenteMapper;
import com.eventhub.utenti_service.service.AuthenticationService;
import com.eventhub.utenti_service.service.GoogleService;
import com.eventhub.utenti_service.service.JwtService;
import com.eventhub.utenti_service.service.RefreshTokenService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("authentication")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final GoogleService googleService;

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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        try {
            Utente authenticatedUser = authenticationService.login(loginRequest);

            String accessToken = jwtService.generateJwtCookie(authenticatedUser);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);

            String jwtRefreshCookie = refreshToken.getToken();

            UserDataResponse userDataResponse = utenteMapper.convert(authenticatedUser);
            LoginResponse response = new LoginResponse(userDataResponse, accessToken, jwtRefreshCookie);

            return ResponseEntity.ok().body(response);

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getReason());
        }
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> authenticateWithGoogle(@RequestBody GoogleAuthRequest request) {

        GoogleTokenResponse tokenResponse = googleService.exchangeCodeForToken(request.getCode());

        GoogleUserInfo userInfo = googleService.getUserInfo(tokenResponse.getAccessToken());

        Utente user = authenticationService.processGoogleUser(userInfo);

        Boolean result = authenticationService.authenticationGoogleUser(user);

        if (!result) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Utente non autenticato!");
        }

        String accessToken = jwtService.generateJwtCookie(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        String jwtRefreshCookie = refreshToken.getToken();

        UserDataResponse userDataResponse = utenteMapper.convert(user);
        LoginResponse response = new LoginResponse(userDataResponse, accessToken, jwtRefreshCookie);

        return ResponseEntity.ok().body(response);

    }

}
