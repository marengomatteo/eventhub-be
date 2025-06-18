package com.eventhub.agenda_service.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "agenda")
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(def = "{'event_id': 1, 'created_at': -1}")
public class Agenda {

    @Id
    private String id;

    @Field("event_id")
    private String eventId;

    @Field("event_name")
    private String eventName;

    @Field("day")
    private LocalDate day;

    @Field("sessions")
    private List<Sessione> sessions = new ArrayList<>();

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}