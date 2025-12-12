package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase {@code Vehicle} representa un vehículo dentro del sistema.
 * Cada vehículo tiene:
 * <ul>
 * <li>{@code id}: identificador único.</li>
 * <li>{@code model}: modelo del vehículo.</li>
 * <li>{@code matricula}: matrícula única.</li>
 * <li>{@code brand}: objeto {@link Brand} al que pertenece el vehículo.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // Marca esta clase como una entidad JPA
@Table(name = "vehicles") // Define el nombre de la tabla asociada a esta entidad
public class Vehicle {

    // Identificador único del vehículo (BIGINT en schema.sql, por eso Long).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Modelo del vehículo (VARCHAR(150) NOT NULL).
    @Column(name = "model", nullable = false, length = 150)
    private String model;

    // Color del vehículo (VARCHAR(50)).
    @Column(name = "color", length = 50)
    private String color;

    // Año de fabricación (INT).
    @Column(name = "year")
    private Integer year;

    // Kilometraje (INT).
    @Column(name = "km")
    private Integer km;

    // Matrícula (VARCHAR(20) UNIQUE).
    @Column(name = "matricula", length = 20, unique = true)
    private String matricula;

    // Marca asociada (Relación N:1).
    // Equivale a la FK `brand_id` en la base de datos.
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchos vehículos a una marca
    @JoinColumn(name = "brand_id", nullable = false) // Clave foránea en la tabla vehicles
    private Brand brand;

    /**
     * Constructor de conveniencia sin {@code id}.
     * Útil para altas donde el ID se autogenera.
     *
     * @param brand Objeto Brand asociado.
     * @param model Modelo del vehículo.
     * @param color Color del vehículo.
     * @param year Año de fabricación.
     * @param km Kilómetros actuales.
     * @param matricula Matrícula del vehículo.
     */
    public Vehicle(Brand brand, String model, String color, Integer year, Integer km, String matricula) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.km = km;
        this.matricula = matricula;
    }
}
