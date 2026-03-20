package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un vehículo registrado en el sistema.
 * Vincula a los usuarios con sus coches y mantiene el historial de citas y reparaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointments", "repairs", "user", "brand"})
@ToString(exclude = {"appointments", "repairs", "user", "brand"})
@Entity
@Table(name = "vehicles")
public class Vehicle {

    /**
     * Identificador único del vehículo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Modelo específico del vehículo (ej. Civic, Golf).
     */
    @Column(name = "model", nullable = false, length = 150)
    private String model;

    /**
     * Número de Identificación del Vehículo (Bastidor).
     * Debe ser único a nivel de base de datos.
     */
    @Column(name = "vin", unique = true, length = 50)
    private String vin;

    /**
     * Color exterior del vehículo.
     */
    @Column(name = "color", length = 50)
    private String color;

    /**
     * Año de fabricación del vehículo.
     */
    @Column(name = "year")
    private Integer year;

    /**
     * Kilometraje actual registrado del vehículo.
     */
    @Column(name = "km")
    private Integer km;

    /**
     * Matrícula o placa del vehículo.
     * Es única y se utiliza frecuentemente para búsquedas rápidas.
     */
    @Column(name = "matricula", unique = true, length = 20)
    private String matricula;

    /**
     * Marca del vehículo (ej. Honda, Toyota).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    /**
     * Usuario propietario del vehículo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Colección de citas programadas para este vehículo.
     */
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Set<Appointment> appointments = new HashSet<>();

    /**
     * Historial de órdenes de reparación realizadas sobre este vehículo.
     */
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Set<Repair> repairs = new HashSet<>();
}