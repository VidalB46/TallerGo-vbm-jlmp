package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA principal que representa a un usuario en el sistema.
 * Contiene la lógica de autenticación, estado de cuenta y sus relaciones de negocio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"roles", "profile", "vehicles", "appointments", "reviews"})
@ToString(exclude = {"roles", "profile", "vehicles", "appointments", "reviews"})
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Correo electrónico, utilizado como identificador de inicio de sesión (username).
     */
    @Column(name = "email", nullable = false, unique = true, length = 40)
    private String email;

    /**
     * Hash de la contraseña del usuario.
     */
    @Column(name = "password_hash", nullable = false, length = 500)
    private String passwordHash;

    /**
     * Indica si la cuenta está activa.
     */
    @Column(name = "active", nullable = false)
    private boolean active;

    /**
     * Indica si la cuenta no ha sido bloqueada (ej. por exceso de intentos fallidos).
     */
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    /**
     * Registro de la última vez que el usuario modificó su contraseña.
     */
    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;

    /**
     * Fecha límite de validez de la contraseña actual.
     */
    @Column(name = "password_expires_at")
    private LocalDateTime passwordExpiresAt;

    /**
     * Contador de intentos de inicio de sesión erróneos.
     */
    @Column(name = "failed_login_attempts", nullable = false)
    private Integer failedLoginAttempts = 0;

    /**
     * Indica si el correo electrónico ha sido confirmado mediante un proceso de verificación.
     */
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    /**
     * Indica si se requiere que el usuario cambie su contraseña en el próximo inicio de sesión.
     */
    @Column(name = "must_change_password", nullable = false)
    private boolean mustChangePassword;

    /**
     * Información detallada del perfil personal del usuario.
     */
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserProfile profile;

    /**
     * Roles de seguridad asignados al usuario.
     * Gestionado mediante la tabla intermedia 'user_roles'.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Vehículos que pertenecen a este usuario.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Vehicle> vehicles = new HashSet<>();

    /**
     * Historial de citas solicitadas por el usuario.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    /**
     * Reseñas y valoraciones publicadas por el usuario.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    /**
     * Constructor para inicializar un usuario con sus parámetros de seguridad.
     * * @param username Email del usuario.
     * @param passwordHash Contraseña ya cifrada.
     * @param active Estado de actividad.
     * @param accountNonLocked Estado de bloqueo.
     * @param lastPasswordChange Fecha de último cambio.
     * @param passwordExpiresAt Fecha de expiración.
     * @param failedLoginAttempts Intentos fallidos iniciales.
     * @param emailVerified Estado de verificación de correo.
     * @param mustChangePassword Obligatoriedad de cambio de contraseña.
     */
    public User(String username, String passwordHash, Boolean active, Boolean accountNonLocked,
                LocalDateTime lastPasswordChange, LocalDateTime passwordExpiresAt,
                Integer failedLoginAttempts, Boolean emailVerified, Boolean mustChangePassword) {
        this.email = username;
        this.passwordHash = passwordHash;
        this.active = active;
        this.accountNonLocked = accountNonLocked;
        this.lastPasswordChange = lastPasswordChange;
        this.passwordExpiresAt = passwordExpiresAt;
        this.failedLoginAttempts = failedLoginAttempts;
        this.emailVerified = emailVerified;
        this.mustChangePassword = mustChangePassword;
    }
}