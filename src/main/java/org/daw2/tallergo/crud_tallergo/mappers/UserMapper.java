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
 * Mapper utilitario para la entidad User.
 * Gestiona la lógica de conversión entre el modelo persistente y los DTOs,
 * incluyendo el aplanamiento (flattening) de los datos del perfil y la gestión de roles.
 */
public class UserMapper {

    // ──────────────────────────────────────────────────────────────────────────
    // Entity → DTO (Conversiones de Salida)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Convierte una entidad User a un DTO básico para listados generales.
     */
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

        // Transformación de Set<Role> a Set<String> (nombres de roles)
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

    /**
     * Convierte una lista de entidades en una lista de DTOs básicos.
     */
    public static List<UserDTO> toDTOList(List<User> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(UserMapper::toDTO).toList();
    }

    /**
     * Convierte la entidad a un DTO de detalle completo.
     * Realiza un "flattening" de la entidad UserProfile para que los campos
     * aparezcan en el primer nivel del DTO.
     */
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

        // Aplanamiento de UserProfile
        UserProfile profile = entity.getProfile();
        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            dto.setPhoneNumber(profile.getPhoneNumber());
            dto.setProfileImage(profile.getProfileImage());
            dto.setBio(profile.getBio());
            dto.setLocale(profile.getLocale());
        }

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

    /**
     * Prepara un DTO de actualización, extrayendo los IDs de los roles.
     */
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

        if (entity.getRoles() != null) {
            Set<Long> roleIds = entity.getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());
            dto.setRoleIds(roleIds);
        }

        return dto;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO → Entity (Conversiones de Entrada)
    // ──────────────────────────────────────────────────────────────────────────

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

    /**
     * Copia datos del DTO a una entidad persistente para actualizaciones parciales.
     */
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

    // --- Métodos sobrecargados para gestión de Roles (usados en Services) ---

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