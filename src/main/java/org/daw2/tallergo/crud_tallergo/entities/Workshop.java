package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA para la tabla 'workshops'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointments", "workshopServices", "reviews"})
@ToString(exclude = {"appointments", "workshopServices", "reviews"})
@Entity
@Table(name = "workshops")
public class Workshop {

    /** INT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** VARCHAR(20) NOT NULL UNIQUE - NIF */
    @Column(name = "nif", nullable = false, unique = true, length = 20)
    private String nif;

    /** VARCHAR(150) NOT NULL */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** VARCHAR(20) NULL */
    @Column(name = "phone", length = 20)
    private String phone;

    /** VARCHAR(255) NULL */
    @Column(name = "location", length = 255)
    private String location;

    /** VARCHAR(100) NULL */
    @Column(name = "email", length = 100)
    private String email;

    /** VARCHAR(100) NULL */
    @Column(name = "schedule", length = 100)
    private String schedule;

    /** Relación 1:N con Appointment */
    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    /** Relación 1:N con Servicios ofrecidos (Tabla intermedia) */
    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    private Set<WorkshopService> workshopServices = new HashSet<>();

    /** Relación 1:N con Reseñas recibidas */
    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Mechanic> mechanics = new HashSet<>();
}