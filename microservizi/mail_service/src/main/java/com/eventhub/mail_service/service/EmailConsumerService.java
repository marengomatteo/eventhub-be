package com.eventhub.mail_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.eventhub.mail_service.dto.EmailRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConsumerService {

    private final EmailSenderService emailService;

    @RabbitListener(queues = "email-queue")
    public void handleUserRegistration(EmailRequest er) {
        try {

            log.info("Received email request from queue to: {}", er.getTo());

            if (er.getSource() != null) {
                log.info("Email request source: {}", er.getSource());

                switch (er.getSource()) {
                    case "USER":
                        log.info("Processing USER email request");
                        processUserEmail(er);
                        break;
                    case "EVENT":
                        log.info("Processing EVENT email request");
                        processEventEmail(er);
                        break;
                    default:
                        log.info("Processing email request from unknown source");
                        emailService.sendEmail(er);
                }
            } else {
                emailService.sendEmail(er);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processUserEmail(EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
    }

    private void processEventEmail(EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
    }
}
