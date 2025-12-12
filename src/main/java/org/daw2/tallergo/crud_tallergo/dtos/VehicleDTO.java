package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico de lectura para Vehicle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long id;
    private String matricula;
    private String model;
    private String color;
    private Integer year;
    private Integer km;

    // Nombre de la marca para mostrar en listados planos
    private String brandName;
}
