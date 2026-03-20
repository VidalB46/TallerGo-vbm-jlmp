package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) utilizado específicamente para la actualización de una Marca existente.
 * <p>
 * A diferencia de los DTOs de creación o lectura, este objeto requiere obligatoriamente
 * el identificador ({@code id}) para saber qué registro de la base de datos se va a modificar.
 * Además, implementa validaciones de Jakarta (Bean Validation) con mensajes
 * internacionalizados para asegurar la integridad de los datos en el controlador
 * antes de pasarlos a la capa de servicio.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandUpdateDTO {

    /**
     * Identificador único de la marca que se desea actualizar.
     * Es estrictamente obligatorio ({@code @NotNull}), ya que sin él el sistema
     * no sabría qué registro modificar.
     */
    @NotNull(message = "{msg.brand.id.notNull}")
    private Integer id;

    /**
     * Nuevo nombre para la marca.
     * Se valida que no sea nulo ni esté compuesto solo por espacios ({@code @NotBlank}),
     * y se restringe su longitud máxima a 100 caracteres para evitar errores en base de datos.
     */
    @NotBlank(message = "{msg.brand.name.notEmpty}")
    @Size(max = 100, message = "{msg.brand.name.size}")
    private String name;

    /**
     * Nuevo país de origen o sede de la marca.
     * Su longitud máxima está restringida a 100 caracteres.
     */
    @Size(max = 100, message = "{msg.brand.country.size}")
    private String country;
}