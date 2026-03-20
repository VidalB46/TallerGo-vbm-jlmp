package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para la solicitud de restablecimiento de contraseña.
 */
@Data
public class PasswordResetRequestDTO {

    /**
     * Correo electrónico del usuario.
     * No puede estar en blanco y debe tener un formato válido.
     */
    @NotBlank
    @Email
    private String email;
}