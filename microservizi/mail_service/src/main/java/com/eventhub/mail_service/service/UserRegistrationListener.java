package com.eventhub.mail_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.eventhub.mail_service.dto.UserEventDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegistrationListener {

    private final MailService emailService;
    
    
    @RabbitListener(queues = "email-queue")
    public void handleUserRegistration(UserEventDto event) {
        try {
            System.out.println("Evento ricevuto in JSON: " + event.getEventType() + 
                               " per utente " + event.getUser().getName()  + 
                               " con email " + event.getUser().getEmail());
            
            if ("REGISTRATION".equals(event.getEventType())) {
                emailService.sendSimpleEmail(
                   event.getUser().getEmail(), 
                   "TEST CODA DI MESSAGGI", 
                   "Test coda di messaggi"
                );
                System.out.println("Email di benvenuto inviata a: " + event.getUser().getEmail());
            }
        } catch (Exception e) {
            System.err.println("Errore nell'elaborazione del messaggio JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}