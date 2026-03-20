package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa a un mecánico en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "workshop")
@ToString(exclude = "workshop")
@Entity
@Table(name = "mechanics")
public class Mechanic {

    /**
     * Identificador único del mecánico.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del mecánico.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Especialidad técnica del mecánico.
     */
    @Column(nullable = false, length = 50)
    private String specialty;

    /**
     * Taller al que pertenece el mecánico.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;
}