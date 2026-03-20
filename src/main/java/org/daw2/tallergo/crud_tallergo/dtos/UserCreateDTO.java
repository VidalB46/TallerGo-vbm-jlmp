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
 * DTO reutilizable para la creación de usuarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    /**
     * Identificador del usuario.
     * Normalmente nulo en la creación, ya que es autogenerado.
     */
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
    private boolean active = Boolean.TRUE;

    /**
     * Indica si la cuenta del usuario está desbloqueada.
     */
    @NotNull(message = "{msg.user.accountNonLocked.notnull}")
    private boolean accountNonLocked = Boolean.TRUE;

    /**
     * Fecha y hora del último cambio de contraseña.
     */
    @PastOrPresent(message = "{msg.user.lastPasswordChange.pastorpresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lastPasswordChange;

    /**
     * Fecha y hora en la que expira la contraseña actual.
     */
    @FutureOrPresent(message = "{msg.user.passwordExpiresAt.futureorpresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime passwordExpiresAt;

    /**
     * Contador de intentos fallidos de inicio de sesión.
     */
    @Min(value = 0, message = "{msg.user.failedLoginAttempts.min}")
    private Integer failedLoginAttempts = 0;

    /**
     * Indica si el correo electrónico del usuario ha sido verificado.
     */
    @NotNull(message = "{msg.user.emailVerified.notnull}")
    private boolean emailVerified = Boolean.FALSE;

    /**
     * Indica si el usuario debe cambiar obligatoriamente su contraseña al iniciar sesión.
     */
    @NotNull(message = "{msg.user.mustChangePassword.notnull}")
    private boolean mustChangePassword = Boolean.FALSE;

    /**
     * Conjunto de identificadores de los roles asignados al usuario.
     */
    @NotEmpty(message = "{msg.user.roles.notempty}")
    private Set<Long> roleIds = new HashSet<>();
}