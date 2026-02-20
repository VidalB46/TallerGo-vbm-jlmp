package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.UserProfileFormDTO;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.UserProfile;

/**
 * Mapper utilitario entre la entidad {@link UserProfile} y su DTO de formulario {@link UserProfileFormDTO}.
 */
public class UserProfileMapper {

    //_________________________________________
    // Entity -> DTO (formulario perfil)
    //__________________________________________

    public static UserProfileFormDTO toFormDto(User user, UserProfile profile) {
        if (user == null) {
            return null;
        }

        UserProfileFormDTO dto = new UserProfileFormDTO();
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());

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

    //_______________________________________
    // DTO (form) -> Entity (create/update)
    //_______________________________________

    public static UserProfile toNewEntity(UserProfileFormDTO dto, User user) {
        if (dto == null || user == null) {
            return null;
        }

        UserProfile profile = new UserProfile();
        profile.setUser(user);

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setProfileImage(dto.getProfileImage());
        profile.setBio(dto.getBio());
        profile.setLocale(dto.getLocale());

        return profile;
    }

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