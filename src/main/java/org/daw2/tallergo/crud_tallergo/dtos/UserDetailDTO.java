package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para visualizar los detalles completos de un usuario.
 * Incluye la información de la cuenta, su perfil y los roles asignados.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {

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
     * Fecha y hora en la que expira la contraseña actual.
     */
    private LocalDateTime passwordExpiresAt;

    /**
     * Contador de intentos fallidos de inicio de sesión.
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
     * Nombre de pila del usuario.
     */
    private String firstName;

    /**
     * Apellidos del usuario.
     */
    private String lastName;

    /**
     * Número de teléfono de contacto.
     */
    private String phoneNumber;

    /**
     * Ruta o URL de la imagen de perfil.
     */
    private String profileImage;

    /**
     * Biografía o descripción corta del usuario.
     */
    private String bio;

    /**
     * Configuración regional o idioma preferido del usuario.
     */
    private String locale;

    /**
     * Conjunto de nombres de los roles asignados al usuario.
     */
    private Set<String> roles;
}