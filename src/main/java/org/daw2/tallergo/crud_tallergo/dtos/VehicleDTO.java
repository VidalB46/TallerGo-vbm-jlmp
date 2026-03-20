package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para la lectura de datos de un vehículo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    /**
     * Identificador único del vehículo.
     */
    private Long id;

    /**
     * Matrícula del vehículo.
     */
    private String matricula;

    /**
     * Modelo del vehículo.
     */
    private String model;

    /**
     * Color del vehículo.
     */
    private String color;

    /**
     * Año de fabricación del vehículo.
     */
    private Integer year;

    /**
     * Kilometraje actual del vehículo.
     */
    private Integer km;

    /**
     * Nombre de la marca a la que pertenece el vehículo.
     */
    private String brandName;
}