package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO genérico para la lectura de datos de un usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Indica si la cuenta del usuario está activa.
     */
    private boolean active;

    /**
     * Indica si la cuenta del usuario está desbloqueada.
     */
    private boolean accountNonLocked;

    /**
     * Fecha y hora del último cambio de contraseña.
     */
    private LocalDateTime lastPasswordChange;

    /**
     * Fecha y hora de expiración de la contraseña actual.
     */
    private LocalDateTime passwordExpiresAt;

    /**
     * Número de intentos fallidos de inicio de sesión.
     */
    private Integer failedLoginAttempts;

    /**
     * Indica si el correo electrónico del usuario ha sido verificado.
     */
    private boolean emailVerified;

    /**
     * Indica si el usuario debe cambiar obligatoriamente su contraseña.
     */
    private boolean mustChangePassword;

    /**
     * Conjunto de nombres de los roles asignados al usuario.
     */
    private Set<String> roles;
}