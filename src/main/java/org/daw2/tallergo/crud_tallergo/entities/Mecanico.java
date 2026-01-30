package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: Leo Morillo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mecanicos")
public class Mecanico {

    // ───────────────────────────────────────────────────────────────
    // CAMPOS PRINCIPALES
    // ───────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** VARCHAR(50) NOT NULL */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /** VARCHAR(50) NOT NULL */
    @Column(name = "especialidad", nullable = false, length = 50)
    private String especialidad;

    // ───────────────────────────────────────────────────────────────
    // RELACIONES
    // ───────────────────────────────────────────────────────────────

    /**
     * Relación N:1 con Taller
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taller_id", nullable = false)
    private Taller taller;
}
