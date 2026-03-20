package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un taller mecánico dentro del sistema.
 * Centraliza la gestión de citas, mecánicos en plantilla y servicios ofrecidos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointments", "workshopServices", "reviews", "mechanics"})
@ToString(exclude = {"appointments", "workshopServices", "reviews", "mechanics"})
@Entity
@Table(name = "workshops")
public class Workshop {

    /**
     * Identificador único del taller.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Número de Identificación Fiscal (NIF/CIF) del taller.
     * Debe ser único y no nulo.
     */
    @Column(name = "nif", nullable = false, unique = true, length = 20)
    private String nif;

    /**
     * Nombre comercial del taller.
     */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /**
     * Teléfono de contacto para clientes.
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * Dirección física o ubicación del taller.
     */
    @Column(name = "location", length = 255)
    private String location;

    /**
     * Correo electrónico de contacto profesional.
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * Descripción del horario de apertura (ej. "L-V: 08:00-18:00").
     */
    @Column(name = "schedule", length = 100)
    private String schedule;

    /**
     * Colección de citas programadas en este taller.
     */
    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    /**
     * Relación con la tabla intermedia que define qué servicios específicos ofrece este taller.
     */
    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    private Set<WorkshopService> workshopServices = new HashSet<>();

    /**
     * Valoraciones y reseñas dejadas por los clientes sobre este taller.
     */
    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    /**
     * Equipo de mecánicos que trabajan actualmente en el taller.
     * Incluye borrado en cascada para la gestión simplificada de la plantilla.
     */
    @OneToMany(mappedBy = "workshop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Mechanic> mechanics = new HashSet<>();
}