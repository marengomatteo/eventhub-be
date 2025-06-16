package com.eventhub.agenda_service.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity(name = "agenda")
@NoArgsConstructor
public class Agenda {
    
    @Id
    @UuidGenerator
    private UUID id;

}
