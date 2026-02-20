package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO de detalle de User.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {

    private Long id;
    private String email;
    private boolean active;
    private boolean accountNonLocked;
    private LocalDateTime lastPasswordChange;
    private LocalDateTime passwordExpiresAt;
    private Integer failedLoginAttempts;
    private boolean emailVerified;
    private boolean mustChangePassword;

    // Campos del perfil del usuario (UserProfile)
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImage;
    private String bio;
    private String locale;

    private Set<String> roles;
}