package com.eventhub.ticket_service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "ticket")
@NoArgsConstructor
@CompoundIndex(def = "{'user_id': 1")
public class Ticket {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("user_name")
    private String userName;

    @Field("user_surname")
    private String userSurname;

    @Field("user_email")
    private String userEmail;

    @Field("event_id")
    private String eventId;

    @Field("event_name")
    private String eventName;

    @Field("start_date")
    private String startDate;

    @Field("location")
    private String location;

    @Version
    private Long version;

}