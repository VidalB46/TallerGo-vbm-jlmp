package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el formulario de edición del perfil de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileFormDTO {

    /**
     * Identificador único del usuario.
     */
    private Long userId;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Nombre de pila del usuario.
     */
    @NotBlank(message = "{msg.userProfile.firstName.notblank}")
    @Size(max = 100, message = "{msg.userProfile.firstName.size}")
    private String firstName;

    /**
     * Apellidos del usuario.
     */
    @NotBlank(message = "{msg.userProfile.lastName.notblank}")
    @Size(max = 100, message = "{msg.userProfile.lastName.size}")
    private String lastName;

    /**
     * Número de teléfono de contacto.
     */
    @Size(max = 30, message = "{msg.userProfile.phoneNumber.size}")
    private String phoneNumber;

    /**
     * Ruta o URL de la imagen de perfil.
     */
    @Size(max = 255, message = "{msg.userProfile.profileImage.size}")
    private String profileImage;

    /**
     * Biografía o descripción corta del usuario.
     */
    @Size(max = 500, message = "{msg.userProfile.bio.size}")
    private String bio;

    /**
     * Configuración regional o idioma preferido del usuario.
     */
    @Size(max = 10, message = "{msg.userProfile.locale.size}")
    private String locale;
}