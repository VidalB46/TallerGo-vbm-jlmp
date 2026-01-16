package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA para la tabla 'users'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"vehicles", "appointments", "reviews"})
@ToString(exclude = {"vehicles", "appointments", "reviews"})
@Entity
@Table(name = "users")
public class User {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** VARCHAR(255) NOT NULL - Nombre Completo (Según PDF) */
    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    /** VARCHAR(20) NULL - Teléfono */
    @Column(name = "phone", length = 20)
    private String phone;

    /** VARCHAR(255) NOT NULL UNIQUE - Email */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    /** VARCHAR(255) NOT NULL - Contraseña */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** * ENUM('CLIENT', 'ADMIN')
     */
    @Column(name = "role", length = 20)
    private String role = "CLIENT";


    /**
     * Relación 1:N con Vehicle.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Vehicle> vehicles = new HashSet<>();

    /**
     * Relación 1:N con Appointment.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    /**
     * Relación 1:N con Review.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    /** Constructor útil */
    public User(String fullName, String phone, String email, String password, String role) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}