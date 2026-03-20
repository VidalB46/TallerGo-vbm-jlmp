package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.UserProfileFormDTO;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.UserProfile;

/**
 * Mapper especializado para la gestión de perfiles de usuario.
 * * A diferencia de otros mappers, este integra datos de la entidad principal (User)
 * y de su extensión (UserProfile) en un único objeto de formulario (DTO),
 * facilitando la edición de datos personales y de cuenta en una sola vista.
 */
public class UserProfileMapper {

    // ──────────────────────────────────────────────────────────────────────────
    // Entity -> DTO (Conversión para Vista de Formulario)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Combina la información de User y UserProfile en un DTO de formulario.
     * * @param user    Entidad base (proporciona ID y Email).
     * @param profile Entidad de detalle (proporciona datos personales).
     * @return UserProfileFormDTO con la información agregada.
     */
    public static UserProfileFormDTO toFormDto(User user, UserProfile profile) {
        if (user == null) {
            return null;
        }

        UserProfileFormDTO dto = new UserProfileFormDTO();
        // Datos provenientes de la tabla 'users'
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());

        // Datos provenientes de la tabla 'user_profiles'
        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            dto.setPhoneNumber(profile.getPhoneNumber());
            dto.setProfileImage(profile.getProfileImage());
            dto.setBio(profile.getBio());
            dto.setLocale(profile.getLocale());
        }

        return dto;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO -> Entity (Conversión para Persistencia)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Crea una nueva entidad UserProfile a partir de los datos del formulario.
     * Establece la relación bidireccional necesaria para JPA.
     */
    public static UserProfile toNewEntity(UserProfileFormDTO dto, User user) {
        if (dto == null || user == null) {
            return null;
        }

        UserProfile profile = new UserProfile();
        // Importante: Vincular con la entidad User propietaria
        profile.setUser(user);

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setProfileImage(dto.getProfileImage());
        profile.setBio(dto.getBio());
        profile.setLocale(dto.getLocale());

        return profile;
    }

    /**
     * Actualiza una entidad de perfil existente con los nuevos valores del formulario.
     * No modifica la relación con User ni los campos de auditoría gestionados por la DB.
     */
    public static void copyToExistingEntity(UserProfileFormDTO dto, UserProfile profile) {
        if (dto == null || profile == null) {
            return;
        }

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setProfileImage(dto.getProfileImage());
        profile.setBio(dto.getBio());
        profile.setLocale(dto.getLocale());
    }
}