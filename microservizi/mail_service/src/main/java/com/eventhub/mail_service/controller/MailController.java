package com.eventhub.mail_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.mail_service.service.MailService;

import com.eventhub.mail_service.dto.EmailRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class MailController {

    private final MailService emailService;

    @PostMapping("/test")
    public String testEmail(@RequestBody EmailRequest request) {
        emailService.sendSimpleEmail(request.getTo(), "benvenuto su eventHub", "sei registrato");
        return "Email inviata con successo a " + request.getTo();
    }
}
