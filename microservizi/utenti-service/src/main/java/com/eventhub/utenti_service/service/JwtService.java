package com.eventhub.utenti_service.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.eventhub.utenti_service.entities.Utente;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.cookie-name}")
    private String jwtAccessCookie;

    @Value("${security.jwt.refresh-cookie-name}")
    private String jwtRefreshCookie;

    @Value("${security.jwt.refresh-expiration-time}")
    private long refreshTokenDuration;

    @Value("${security.jwt.reset-password-expiration-time}")
    private long resetPasswordTokenExpirationTime;

    public ResponseCookie generateJwtCookie(Utente userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return generateCookie(jwtAccessCookie, jwt, "/", jwtExpiration);
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(jwtRefreshCookie, refreshToken, "/", refreshTokenDuration);
    }

    public ResponseCookie getCleanJwtCookie() {
        return generateCookie(jwtAccessCookie, null, "/", 0);

    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        return generateCookie(jwtRefreshCookie, null, "/", 0);
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtAccessCookie);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (jwtExpiration * 1000)))
                .signWith(getSignInKey())
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserId(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public long getRefreshTokenDuration() {
        return this.refreshTokenDuration;
    }

    public long getResetPasswordTokenDuration() {
        return this.resetPasswordTokenExpirationTime;
    }

    public String getRefreshTokenName() {
        return this.jwtRefreshCookie;
    }

    public String getAccessTokenName() {
        return this.jwtAccessCookie;
    }

    /* Private function */

    private ResponseCookie generateCookie(String name, String value, String path, long maxAge) {
        return ResponseCookie
                .from(name, value)
                .path(path)
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(true)
                .sameSite("strict")
                .build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}