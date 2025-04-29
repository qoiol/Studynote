package com.example.postservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "post")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "updated \"post\" set deleted_at = now() where id = ?")
@SQLRestriction("deleted_at is null")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    @Column(name = "content", columnDefinition = "text not null")
    private String content;
    @Column(name="registered_at")
    private Timestamp registeredAt;
    @Column(name="updated_at")
    private Timestamp updatedAt;
    @Column(name="deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    public void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
