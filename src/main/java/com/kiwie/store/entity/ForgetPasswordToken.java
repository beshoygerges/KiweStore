package com.kiwie.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ForgetPasswordToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID token = UUID.randomUUID();

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false)
    private LocalDateTime expirationDateTime = LocalDateTime.now().plusHours(24);

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne
    private User user;

    public boolean isExpired() {
        return expirationDateTime.isBefore(LocalDateTime.now());
    }
}
