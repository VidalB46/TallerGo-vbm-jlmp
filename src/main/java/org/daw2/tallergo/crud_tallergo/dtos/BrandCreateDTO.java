package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reutilizable para crear marcas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandCreateDTO {

    // En este DTO este campo siempre vendrá null porque es una inserción
    private Integer id;

    @NotBlank(message = "{msg.brand.name.notEmpty}")
    @Size(max = 100, message = "{msg.brand.name.size}")
    private String name;

    @Size(max = 100, message = "{msg.brand.country.size}")
    private String country;
}