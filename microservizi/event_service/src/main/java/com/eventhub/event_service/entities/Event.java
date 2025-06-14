package com.eventhub.event_service.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "event")
@NoArgsConstructor
public class Event {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String date; //TODO: come gestiiamo se è un evento che copre piu' giorni?

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String maxPartecipants;

    // @Column(nullable = false)
    // private List partecipantsList; // lista di utenti che partecipano all'evento

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private String event_type; // concerto/conferenza/altro  

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    // private List<RefreshToken> refreshTokens;

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     return Collections.singletonList(
    //             new SimpleGrantedAuthority(this.role.name()));
    // }
}
