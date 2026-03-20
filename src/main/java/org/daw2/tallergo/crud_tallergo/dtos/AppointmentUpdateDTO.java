package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;

/**
 * DTO para la modificación de una cita existente.
 * Permite principalmente al taller gestionar el estado y las fechas de la cita.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentUpdateDTO extends AppointmentCreateDTO {

    @NotNull(message = "{msg.appointment.id.notNull}")
    private Long id;

    @NotNull(message = "{msg.appointment.status.notNull}")
    private AppointmentStatus status;
}