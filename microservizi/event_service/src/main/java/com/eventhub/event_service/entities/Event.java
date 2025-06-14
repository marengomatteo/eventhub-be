package com.eventhub.event_service.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
    private String maxPartecipants;

    // @Column(nullable = false)
    // private List partecipantsList; // lista di utenti che partecipano all'evento

    @Field
    private String eventType; // concerto/conferenza/altro

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

}
