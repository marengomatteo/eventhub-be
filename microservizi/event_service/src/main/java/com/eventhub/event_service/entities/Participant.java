package com.eventhub.event_service.entities;

import lombok.Data;

@Data
public class Participant {

    private String userId;
    private String name;
    private String surname;
    private String email;

}
