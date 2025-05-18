package main.java.com.eventhub.mail_service.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity(name = "tickets")
@NoArgsConstructor
public class Ticket {
    
    @Id
    private String id;

    @Column(nullable = false)
    private Utente user;

    // @Column(nullable = false)
    // private Event event;

    private boolean isUsed = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
