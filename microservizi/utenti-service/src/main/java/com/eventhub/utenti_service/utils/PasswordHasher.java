package com.eventhub.utenti_service.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PasswordHasher {

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Cripta una password utilizzando l'algoritmo BCrypt
     */
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    /**
     * Verifica se una password in chiaro corrisponde a una password criptata.
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
}