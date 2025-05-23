package com.eventhub.mail_service.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.eventhub.mail_service.dto.EmailRequest;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;

    private String fromEmail = "noreply@eventhub.com";

    @Value("${app.email.logo.path:static/images/logo-expanded.png}")
    private String logoPath;

    private static final String EMAIL_FOOTER = "<br><br><hr style='margin: 20px 0; border: none; border-top: 1px solid #ddd;'>"
            +
            "<p style='font-size: 12px; color: #666; text-align: center; margin: 10px 0;'>" +
            "Questo messaggio è generato automaticamente, non rispondere a questo indirizzo email, il team eventhub." +
            "</p>";

    public void sendEmail(EmailRequest emailRequest) {
        try {
            log.info("Preparing to send email to: {}", emailRequest.getTo());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailRequest.getTo());
            helper.setFrom(fromEmail);
            helper.setSubject(emailRequest.getSubject());

            String htmlBody = buildHtmlEmailBody(emailRequest.getBody());
            helper.setText(htmlBody, true);

            // Aggiunge il logo in header
            ClassPathResource logoResource = new ClassPathResource(logoPath);
            if (logoResource.exists()) {
                helper.addInline("eventhubLogo", logoResource);
                log.info("Logo attached successfully");
            } else {
                log.warn("Logo file not found at path: {}", logoPath);
            }

            if (emailRequest.getTemplateData().containsKey("attachment")) {
                helper.addAttachment("ticket.png", addAttachment(emailRequest.getTemplateData()));
            }
            // Invia l'email
            mailSender.send(mimeMessage);
            log.info("Email sent successfully to: {}", emailRequest.getTo());

        } catch (MessagingException e) {
            log.error("Error sending email to {}: {}", emailRequest.getTo(), e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private ByteArrayResource addAttachment(Map<String, Object> templateData) {
        try {
            String attachmentValue = templateData.get("attachment").toString();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(attachmentValue, com.google.zxing.BarcodeFormat.QR_CODE, 250,
                    250);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return new ByteArrayResource(pngOutputStream.toByteArray(), "image/png");
        } catch (WriterException | IOException e) {
            log.error("Error add attachment {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Costruisce il corpo HTML dell'email con logo e footer
     */
    private String buildHtmlEmailBody(String messageBody) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<!DOCTYPE html>")
                .append("<html lang='it'>")
                .append("<head>")
                .append("<meta charset='UTF-8'>")
                .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
                .append("<title>EventHub</title>")
                .append("</head>")
                .append("<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0 auto; padding: 20px;'>");

        htmlBuilder.append("<div style='background-color: #f9f9f9; padding: 20px; border-radius: 10px;'>");

        // Header con logo
        htmlBuilder.append("<div style='text-align: center; margin-bottom: 30px;'>")
                .append("<img src='cid:eventhubLogo' alt='EventHub Logo' style='max-width:200px; height: auto;' />")
                .append("</div>");

        // Contenuto
        htmlBuilder.append(
                "<div style='background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>");

        // Converte il messaggio in HTML
        String htmlMessage = convertTextToHtml(messageBody);
        htmlBuilder.append(htmlMessage);

        // Footer
        htmlBuilder.append(EMAIL_FOOTER);

        // Chiude html
        htmlBuilder.append("</div></div></body></html>");

        return htmlBuilder.toString();
    }

    private String convertTextToHtml(String text) {
        if (text == null) {
            return "";
        }

        // Se il testo contiene già tag HTML, restituiscilo così com'è
        if (text.trim().startsWith("<") && text.trim().endsWith(">")) {
            return text;
        }

        // Converti il testo semplice in HTML
        return "<p style='margin: 0 0 15px 0; font-size: 16px; line-height: 1.5;'>" +
                text.replace("\n\n", "</p><p style='margin: 0 0 15px 0; font-size: 16px; line-height: 1.5;'>")
                        .replace("\n", "<br>")
                +
                "</p>";
    }
}