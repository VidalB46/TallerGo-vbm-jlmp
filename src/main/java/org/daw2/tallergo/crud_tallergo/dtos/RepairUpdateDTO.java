package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;

/**
 * DTO para actualizar el progreso de una reparación.
 * Utilizado por el mecánico para cambiar estados (ACTIVO, FINALIZADO) y añadir notas.
 */
@Data
public class RepairUpdateDTO {

    @NotNull(message = "{msg.repair.id.notNull}")
    private Long id;

    @NotNull(message = "{msg.repair.status.notNull}")
    private RepairStatus status;

    private String notes;
}