package com.eventhub.mail_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.mail_service.service.MailService;
import com.eventhub.mail_service.dto.EmailRequest;
// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/test")
    public void testEmail(@RequestBody EmailRequest request) {
        mailService.sendSimpleEmail(request.getTo(), "benvenuto su eventHub", "sei registrato");
    }

    @PostMapping("/ticketTest")
    public void testEmailTicket(@RequestBody EmailRequest request) {
        String ticketId = UUID.randomUUID().toString();
        mailService.sendEmailWithAttachment(request.getTo(), "Ticket", "biglietto", ticketId);
    }
}
