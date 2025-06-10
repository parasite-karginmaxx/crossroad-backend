package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    private boolean confirmed;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

