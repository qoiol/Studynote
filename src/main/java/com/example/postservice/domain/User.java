package com.example.postservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@ToString
@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "updated \"user\" set deleted_at = now() where id=?")
@SQLRestriction("deleted_at is null")
public class User {
    @Id
    @Column(name = "id", nullable = false, unique = true, length = 30)
    private String id;
    @Column(name = "password", nullable = false, length = 300)
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    @Column(name = "registered_at")
    private Timestamp registeredAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name="deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
