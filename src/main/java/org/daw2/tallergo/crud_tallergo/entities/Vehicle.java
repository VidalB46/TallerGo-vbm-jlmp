package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA para la tabla 'vehicles'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointments", "repairs", "user", "brand"})
@ToString(exclude = {"appointments", "repairs", "user", "brand"})
@Entity
@Table(name = "vehicles")
public class Vehicle {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** VARCHAR(150) NOT NULL - Modelo del coche */
    @Column(name = "model", nullable = false, length = 150)
    private String model;

    /** VARCHAR(50) UNIQUE - Número de Bastidor */
    @Column(name = "vin", unique = true, length = 50)
    private String vin;

    /** VARCHAR(50) NULL - Color */
    @Column(name = "color", length = 50)
    private String color;

    /** INT NULL - Año fabricación */
    @Column(name = "year")
    private Integer year;

    /** INT NULL - Kilómetros actuales */
    @Column(name = "km")
    private Integer km;

    /** VARCHAR(20) UNIQUE - Matrícula */
    @Column(name = "matricula", unique = true, length = 20)
    private String matricula;

    /** Relación N:1 con Brand (Marca) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    /** Relación N:1 con User (Dueño) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Relación 1:N con Appointment (Historial de citas) */
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    /** Relación 1:N con Repair (Historial de reparaciones) */
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Set<Repair> repairs = new HashSet<>();
}