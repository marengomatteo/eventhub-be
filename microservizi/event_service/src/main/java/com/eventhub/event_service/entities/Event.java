package com.eventhub.event_service.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    private String id;

    @Field
    private String eventName;

    @Field
    private String startDate;

    @Field
    private String endDate;

    @Field
    private String time;

    @Field
    private String location;

    @Field
    private String description;

    @Field
    private int maxPartecipants;

    @Field
    private List<Participant> partecipantsList;

    @Field
    private String eventType; // concerto/conferenza/altro

}
