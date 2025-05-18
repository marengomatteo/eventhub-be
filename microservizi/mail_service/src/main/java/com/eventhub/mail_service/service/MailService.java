package com.eventhub.mail_service.service;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
   
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply.eventhub@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    public void sendEmailWithAttachment(String to, String subject, String text, String ticketId) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(ticketId, com.google.zxing.BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        // SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = mailSender.createMimeMessage();
        
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("noreply.eventhub@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment("ticket.png", new ByteArrayResource(pngOutputStream.toByteArray()), "image/png");
        
        mailSender.send(message);
    }
}
