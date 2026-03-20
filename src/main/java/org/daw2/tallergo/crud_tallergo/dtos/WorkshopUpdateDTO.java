package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO para la actualización de un taller.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkshopUpdateDTO extends WorkshopCreateDTO {

    /**
     * Identificador único del taller a actualizar.
     * Es obligatorio para identificar el registro.
     */
    @NotNull(message = "{msg.workshop.id.notNull}")
    private Integer id;
}