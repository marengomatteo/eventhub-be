package com.eventhub.mail_service.controller;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.mail_service.service.MailService;

import com.eventhub.mail_service.dto.EmailRequest;

import lombok.RequiredArgsConstructor;
import main.java.com.eventhub.mail_service.entities.Ticket;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class MailController {

    private final MailService emailService;

    @PostMapping("/test")
    public testEmail(@RequestBody EmailRequest request) {
        emailService.sendSimpleEmail(request.getTo(), "benvenuto su eventHub", "sei registrato");
    }


    @PostMapping("/ticketTest")
    public testEmailTicket(@RequestBody EmailRequest request) {
        String ticketId = UUID.randomUUID().toString();
        // Ticket ticket = new Ticket(request.getUser(), request.getEvent(), ticketId);
        emailService.sendEmailWithAttachment(request.getTo(), "Ticket", "biglietto", ticketId);
    }
}
