package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) utilizado para el proceso de restablecimiento de contraseña.
 * <p>
 * Este objeto encapsula la información necesaria que envía el usuario (o el cliente web/móvil)
 * cuando desea establecer una nueva contraseña tras haberla olvidado. Incluye el token de
 * seguridad para verificar su identidad y los campos para la nueva contraseña con su confirmación.
 * </p>
 */
@Data
public class PasswordResetDTO {

    /**
     * Token de seguridad único de un solo uso, enviado generalmente al correo electrónico
     * del usuario. Es obligatorio ({@code @NotBlank}) para autorizar el cambio de contraseña.
     */
    @NotBlank
    private String token;

    /**
     * La nueva contraseña elegida por el usuario.
     * Es obligatoria y debe cumplir con estrictos requisitos de longitud (mínimo 8 caracteres
     * por seguridad básica, y máximo 72 caracteres). El límite de 72 caracteres es una buena
     * práctica de seguridad diseñada específicamente para evitar vulnerabilidades al utilizar
     * algoritmos de hashing como Bcrypt, los cuales truncan las entradas a 72 bytes.
     */
    @NotBlank
    @Size(min = 8, max = 72)
    private String newPassword;

    /**
     * Confirmación de la nueva contraseña introducida por el usuario.
     * Obligatoria ({@code @NotBlank}) y utilizada típicamente a nivel de controlador o servicio
     * para asegurar que coincida exactamente con {@code newPassword}, evitando así errores
     * tipográficos por parte del usuario.
     */
    @NotBlank
    private String confirmPassword;
}