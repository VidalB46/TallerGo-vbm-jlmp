package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa los tokens de seguridad para el restablecimiento de contraseñas.
 */
@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    /**
     * Identificador único del registro del token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario propietario del token de recuperación.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Valor hash (SHA-256) del token para su validación segura.
     */
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    /**
     * Fecha y hora de expiración del token.
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Fecha y hora en la que el token fue utilizado efectivamente.
     */
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    /**
     * Fecha y hora de creación del registro.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Dirección IP del cliente que realizó la solicitud de restablecimiento.
     */
    @Column(name = "request_ip", length = 45)
    private String requestIp;

    /**
     * Información del navegador o cliente que solicitó el token.
     */
    @Column(name = "user_agent", length = 255)
    private String userAgent;

    /**
     * Comprueba si el token ha superado su fecha de validez.
     * @return true si la fecha actual es posterior a expiresAt.
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Comprueba si el token ya ha sido canjeado.
     * @return true si usedAt contiene una fecha.
     */
    public boolean isUsed() {
        return usedAt != null;
    }
}