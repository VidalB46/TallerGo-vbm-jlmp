package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla 'password_reset_tokens'.
 */
@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    /** Identificador único del token de recuperación (PK autoincremental). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario propietario del token. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Hash del token (SHA-256 en hex). */
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    /** Fecha y hora límite hasta la que el token es válido. */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /** Fecha y hora en la que el token se consumió. */
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    /** Fecha y hora de creación del token. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** IP desde la que se solicitó el reset. */
    @Column(name = "request_ip", length = 45)
    private String requestIp;

    /** User-Agent del cliente que solicitó el reset. */
    @Column(name = "user_agent", length = 255)
    private String userAgent;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }
}