package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de una marca.
 * Se utiliza para recibir los datos del formulario de creación de marca
 * y validarlos antes de persistirlos en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandCreateDTO {

    /**
     * ID de la marca.
     * Se genera automáticamente en la base de datos; puede ser nulo al crear.
     */
    private Integer id;

    /**
     * Nombre de la marca.
     * No puede estar vacío y tiene un máximo de 100 caracteres.
     */
    @NotBlank(message = "{msg.brand.name.notEmpty}")
    @Size(max = 100, message = "{msg.brand.name.size}")
    private String name;

    /**
     * País de origen de la marca.
     * Es opcional y tiene un máximo de 100 caracteres.
     */
    @Size(max = 100, message = "{msg.brand.country.size}")
    private String country;
}