package com.eventhub.utenti_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("utente")
public class UtentiServiceController {

    @GetMapping("/hello")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("hello");
    }

}
