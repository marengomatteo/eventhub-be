package com.eventhub.utenti_service.configuration;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eventhub.utenti_service.service.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String accessTokenString = jwtService.getJwtFromCookies(request);
        String refreshTokenString = jwtService.getJwtRefreshFromCookies(request);

        if (accessTokenString == null) {
            if (StringUtils.hasText(refreshTokenString)) {
                log.debug("fare refreshTokenService");
                // refreshTokenService.manageExpiredAccessToken(request, response,
                // refreshTokenString);
            }
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtService.extractUserId(accessTokenString);

            if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(accessTokenString, userDetails)) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);

            } else if (StringUtils.hasText(refreshTokenString)) {
                log.debug("fare refreshTokenService");
                // refreshTokenService.manageExpiredAccessToken(request, response,
                // refreshTokenString);
            }

        } catch (JwtException e) {
            log.error("Cannot set user authentication: {}", e);
            log.warn("Invalid JWT token: " + e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }

    }

}
