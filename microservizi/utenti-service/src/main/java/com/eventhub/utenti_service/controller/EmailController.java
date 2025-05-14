package com.eventhub.utenti_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.utenti_service.dto.mail.EmailRequest;
import com.eventhub.utenti_service.service.EmailService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/test")
    public String testEmail(@RequestBody EmailRequest request) {
        emailService.sendSimpleEmail(request.getTo(), "benvenuto su eventHub", "sei registrato");
        return "Email inviata con successo a " + request.getTo();
    }
} 


