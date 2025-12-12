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
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model", nullable = false, length = 150)
    private String model;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "year")
    private Integer year;

    @Column(name = "km")
    private Integer km;

    @Column(name = "matricula", length = 20, unique = true)
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public Vehicle(Brand brand, String model, String color, Integer year, Integer km, String matricula) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.year = year;
        this.km = km;
        this.matricula = matricula;
    }
}
