package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) diseñado específicamente para la actualización de un Mecánico existente.
 * <p>
 * Al igual que ocurre con las marcas, este DTO obliga a enviar el identificador ({@code id})
 * para localizar al mecánico en la base de datos antes de modificarlo. Además, implementa
 * estrictas reglas de validación (Bean Validation) con mensajes internacionalizados para
 * proteger la integridad de los datos (evitando nombres vacíos o textos que superen el límite
 * de la base de datos).
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MechanicUpdateDTO {

    /**
     * Identificador único del mecánico que se va a actualizar.
     * Es totalmente obligatorio ({@code @NotNull}) para que el sistema sepa a qué registro
     * aplicarle los cambios.
     */
    @NotNull(message = "{msg.mechanic.id.notNull}")
    private Long id;

    /**
     * Nuevo nombre completo del mecánico.
     * Se valida que no esté en blanco ({@code @NotBlank}) y que su longitud no supere
     * los 100 caracteres para evitar desbordamientos en la columna de la base de datos.
     */
    @NotBlank(message = "{msg.mechanic.name.notEmpty}")
    @Size(max = 100, message = "{msg.mechanic.name.size}")
    private String name;

    /**
     * Nueva especialidad técnica del mecánico.
     * Es un campo requerido ({@code @NotBlank}) y su longitud está limitada a un máximo
     * de 50 caracteres.
     */
    @NotBlank(message = "{msg.mechanic.specialty.notEmpty}")
    @Size(max = 50, message = "{msg.mechanic.specialty.size}")
    private String specialty;

    /**
     * Identificador del taller (Workshop) al que estará asociado el mecánico tras la actualización.
     * Se exige que no sea nulo ({@code @NotNull}) para asegurar que el mecánico siempre
     * pertenezca a un taller válido.
     */
    @NotNull(message = "{msg.mechanic.workshop.notNull}")
    private Integer workshopId;
}