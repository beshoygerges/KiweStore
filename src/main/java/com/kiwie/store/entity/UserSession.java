package com.kiwie.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID sessionId = UUID.randomUUID();

    private boolean enabled = true;

    private LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(15);

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JoinColumn(name = "userId",referencedColumnName = "id")
    @ManyToOne
    private User user;

    public boolean isExpired() {
        return expirationDateTime.isBefore(LocalDateTime.now());
    }
}
