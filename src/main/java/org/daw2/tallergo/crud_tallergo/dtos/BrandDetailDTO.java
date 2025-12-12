package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de detalle de Brand. Incluye la lista de vehículos asociados.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDetailDTO {
    private Integer id;
    private String name;
    private String country;

    // Lista de vehículos (usamos el DTO genérico de vehículo)
    private List<VehicleDTO> vehicles;
}
