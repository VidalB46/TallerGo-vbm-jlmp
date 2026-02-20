package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * author: Leo Morillo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "talleres")
public class Taller {

    // ───────────────────────────────────────────────────────────────
    // CAMPOS PRINCIPALES
    // ───────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** VARCHAR(50) NOT NULL */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /** VARCHAR(100) */
    @Column(name = "direccion", length = 100)
    private String direccion;

    // ───────────────────────────────────────────────────────────────
    // RELACIONES
    // ───────────────────────────────────────────────────────────────

    /**
     * Relación 1:N con Mecánico
     */
    @OneToMany(mappedBy = "taller", fetch = FetchType.LAZY)
    private Set<Mecanico> mecanicos = new HashSet<>();
}