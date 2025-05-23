package com.eventhub.mail_service.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventhub.mail_service.dto.EmailRequest;
import com.eventhub.mail_service.service.EmailSenderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class MailController {

    private final EmailSenderService mailService;

    @PostMapping("/test")
    public void testTicketEmail(@RequestBody EmailRequest request) {
        EmailRequest er = new EmailRequest(request.getTo(), request.getSubject(),
                request.getBody(), request.getSource(), Optional.empty(), request.getTemplateData());
        mailService.sendEmail(er);
    }

}
