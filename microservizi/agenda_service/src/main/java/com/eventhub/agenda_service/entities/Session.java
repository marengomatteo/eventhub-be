package com.eventhub.agenda_service.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "sessioni")
@NoArgsConstructor
public class Session {
    
    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String speaker;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
