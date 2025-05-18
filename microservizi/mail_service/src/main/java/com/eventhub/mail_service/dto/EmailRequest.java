package com.eventhub.mail_service.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;

    public EmailRequest() {}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
