package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.UserProfileFormDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interfaz de servicio para la gestión de perfiles de usuario.
 * Proporciona los métodos necesarios para la visualización y edición
 * de la información personal de los usuarios en TallerGo.
 */
public interface UserProfileService {

    /**
     * Recupera la información de perfil necesaria para rellenar un formulario.
     * Combina datos de la cuenta (email) con datos personales (nombre, bio, etc.).
     * * @param email Email del usuario cuyo perfil se desea recuperar.
     * @return DTO de formulario con la información agregada.
     */
    UserProfileFormDTO getFormByEmail(String email);

    /**
     * Actualiza la información del perfil de un usuario.
     * Gestiona tanto los campos de texto como la subida opcional de una nueva imagen de perfil.
     * * @param email            Email del usuario que realiza la actualización.
     * @param profileDto       Datos del formulario (nombre, apellidos, bio, etc.).
     * @param profileImageFile Archivo de imagen opcional (Multipart).
     */
    void updateProfile(String email, UserProfileFormDTO profileDto, MultipartFile profileImageFile);
}