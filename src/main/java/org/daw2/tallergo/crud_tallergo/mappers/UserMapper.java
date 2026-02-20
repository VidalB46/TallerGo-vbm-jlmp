package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Role;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.UserProfile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper utilitario entre la entidad {@link User} y sus DTOs.
 */
public class UserMapper {

    // ─────────────────────────────────────────
    // Entity → DTO (listado/tabla básico)
    // ─────────────────────────────────────────

    public static UserDTO toDTO(User entity) {
        if (entity == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        dto.setAccountNonLocked(entity.isAccountNonLocked());
        dto.setLastPasswordChange(entity.getLastPasswordChange());
        dto.setPasswordExpiresAt(entity.getPasswordExpiresAt());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setEmailVerified(entity.isEmailVerified());
        dto.setMustChangePassword(entity.isMustChangePassword());

        // Cargar roles si existen
        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            Set<String> roleNames = entity.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        } else {
            dto.setRoles(new HashSet<>());
        }

        return dto;
    }

    public static List<UserDTO> toDTOList(List<User> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(UserMapper::toDTO).toList();
    }

    // ─────────────────────────────────────────
    // Entity → DTO (detalle)
    // ─────────────────────────────────────────

    public static UserDetailDTO toDetailDTO(User entity) {
        if (entity == null) return null;

        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        dto.setAccountNonLocked(entity.isAccountNonLocked());
        dto.setLastPasswordChange(entity.getLastPasswordChange());
        dto.setPasswordExpiresAt(entity.getPasswordExpiresAt());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setEmailVerified(entity.isEmailVerified());
        dto.setMustChangePassword(entity.isMustChangePassword());

        // Cargar datos del perfil si existe
        UserProfile profile = entity.getProfile();
        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            dto.setPhoneNumber(profile.getPhoneNumber());
            dto.setProfileImage(profile.getProfileImage());
            dto.setBio(profile.getBio());
            dto.setLocale(profile.getLocale());
        }

        // Cargar roles si existen
        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            Set<String> roleNames = entity.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        } else {
            dto.setRoles(new HashSet<>());
        }

        return dto;
    }

    public static UserUpdateDTO toUpdateDTO(User entity) {
        if (entity == null) return null;

        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        dto.setAccountNonLocked(entity.isAccountNonLocked());
        dto.setLastPasswordChange(entity.getLastPasswordChange());
        dto.setPasswordExpiresAt(entity.getPasswordExpiresAt());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setEmailVerified(entity.isEmailVerified());
        dto.setMustChangePassword(entity.isMustChangePassword());

        // Rellenar roleIds a partir de entity.roles
        if (entity.getRoles() != null) {
            Set<Long> roleIds = entity.getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());
            dto.setRoleIds(roleIds);
        }

        return dto;
    }

    // ─────────────────────────────────────────
    // DTO (create/update) → Entity
    // ─────────────────────────────────────────

    public static User toEntity(UserCreateDTO dto) {
        if (dto == null) return null;

        User e = new User();
        e.setEmail(dto.getEmail());
        e.setActive(dto.isActive());
        e.setAccountNonLocked(dto.isAccountNonLocked());
        e.setLastPasswordChange(dto.getLastPasswordChange());
        e.setPasswordExpiresAt(dto.getPasswordExpiresAt());
        e.setFailedLoginAttempts(dto.getFailedLoginAttempts());
        e.setEmailVerified(dto.isEmailVerified());
        e.setMustChangePassword(dto.isMustChangePassword());
        return e;
    }

    public static User toEntity(UserUpdateDTO dto) {
        if (dto == null) return null;

        User e = new User();
        e.setId(dto.getId());
        e.setEmail(dto.getEmail());
        e.setActive(dto.isActive());
        e.setAccountNonLocked(dto.isAccountNonLocked());
        e.setLastPasswordChange(dto.getLastPasswordChange());
        e.setPasswordExpiresAt(dto.getPasswordExpiresAt());
        e.setFailedLoginAttempts(dto.getFailedLoginAttempts());
        e.setEmailVerified(dto.isEmailVerified());
        e.setMustChangePassword(dto.isMustChangePassword());
        return e;
    }

    public static void copyToExistingEntity(UserUpdateDTO dto, User entity) {
        if (dto == null || entity == null) return;

        entity.setEmail(dto.getEmail());
        entity.setActive(dto.isActive());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setLastPasswordChange(dto.getLastPasswordChange());
        entity.setPasswordExpiresAt(dto.getPasswordExpiresAt());
        entity.setFailedLoginAttempts(dto.getFailedLoginAttempts());
        entity.setEmailVerified(dto.isEmailVerified());
        entity.setMustChangePassword(dto.isMustChangePassword());
    }

    public static User toEntity(UserCreateDTO dto, Set<Role> roles) {
        if (dto == null) return null;
        User e = toEntity(dto);
        e.setRoles(roles);
        return e;
    }

    public static User toEntity(UserUpdateDTO dto, Set<Role> roles) {
        if (dto == null) return null;
        User e = toEntity(dto);
        e.setRoles(roles);
        return e;
    }

    public static void copyToExistingEntity(UserUpdateDTO dto, User entity, Set<Role> roles) {
        if (dto == null || entity == null) return;
        copyToExistingEntity(dto, entity);
        entity.setRoles(roles);
    }
}