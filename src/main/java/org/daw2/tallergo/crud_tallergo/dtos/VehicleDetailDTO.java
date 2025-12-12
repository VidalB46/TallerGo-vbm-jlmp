package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de detalle de Vehículo.
 * Incluye la marca a la que pertenece para poder navegar desde el detalle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetailDTO {

    private Long id;
    private String matricula;
    private String model;
    private String color;
    private Integer year;
    private Integer km;

    /** Marca asociada al vehículo (objeto embebido para el detalle). */
    private BrandDTO brand;
}