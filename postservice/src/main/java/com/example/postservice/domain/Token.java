package com.example.postservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@Entity
@Table(name = "token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("expires_at > now()")
public class Token {
    @Id
    @Column(name = "token")
    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "expires_at")
    private Timestamp expiresAt;
    @Column(name = "last_used_at")
    private Timestamp lastUsedAt;
}
