package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO reutilizable para la actualización de usuarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    /**
     * Identificador único del usuario a actualizar.
     * Es obligatorio para identificar el registro.
     */
    @NotNull(message = "{msg.user.id.notnull}")
    private Long id;

    /**
     * Correo electrónico del usuario, utilizado como nombre de usuario.
     */
    @Email(message = "{msg.user.email.invalid}")
    @NotBlank(message = "{msg.user.username.notblank}")
    @Size(min = 4, max = 100, message = "{msg.user.username.size}")
    private String email;

    /**
     * Indica si la cuenta del usuario está activa.
     */
    @NotNull(message = "{msg.user.active.notnull}")
    private boolean active;

    /**
     * Indica si la cuenta del usuario está desbloqueada.
     */
    @NotNull(message = "{msg.user.accountNonLocked.notnull}")
    private boolean accountNonLocked;

    /**
     * Fecha y hora del último cambio de contraseña.
     */
    @PastOrPresent(message = "{msg.user.lastPasswordChange.pastorpresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastPasswordChange;

    /**
     * Fecha y hora de expiración de la contraseña actual.
     */
    @FutureOrPresent(message = "{msg.user.passwordExpiresAt.futureorpresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime passwordExpiresAt;

    /**
     * Contador de intentos fallidos de inicio de sesión.
     */
    @Min(value = 0, message = "{msg.user.failedLoginAttempts.min}")
    private Integer failedLoginAttempts;

    /**
     * Indica si el correo electrónico del usuario ha sido verificado.
     */
    @NotNull(message = "{msg.user.emailVerified.notnull}")
    private boolean emailVerified;

    /**
     * Indica si el usuario debe cambiar obligatoriamente su contraseña.
     */
    @NotNull(message = "{msg.user.mustChangePassword.notnull}")
    private boolean mustChangePassword;

    /**
     * Conjunto de identificadores de los roles asignados al usuario.
     */
    @NotEmpty(message = "{msg.user.roles.notempty}")
    private Set<Long> roleIds = new HashSet<>();
}