package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reutilizable para crear vehículos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCreateDTO {

    // ID nulo en inserción
    private Long id;

    @NotBlank(message = "{msg.vehicle.matricula.notEmpty}")
    @Size(max = 20, message = "{msg.vehicle.matricula.size}")
    private String matricula;

    @NotBlank(message = "{msg.vehicle.model.notEmpty}")
    @Size(max = 150, message = "{msg.vehicle.model.size}")
    private String model;

    @Size(max = 50, message = "{msg.vehicle.color.size}")
    private String color;

    @NotNull(message = "{msg.vehicle.year.notNull}")
    @Min(value = 1900, message = "{msg.vehicle.year.min}")
    @Max(value = 2100, message = "{msg.vehicle.year.max}")
    private Integer year;

    @NotNull(message = "{msg.vehicle.km.notNull}")
    @Min(value = 0, message = "{msg.vehicle.km.min}")
    private Integer km;

    // Relación con Marca (FK)
    @NotNull(message = "{msg.vehicle.brand.notNull}")
    private Integer brandId;
}