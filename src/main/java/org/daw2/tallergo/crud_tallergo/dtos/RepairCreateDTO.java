package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO para la apertura de una orden de reparación.
 * Normalmente generado por un mecánico a partir de una cita confirmada.
 */
@Data
public class RepairCreateDTO {

    /**
     * Fecha de entrada del vehículo al taller.
     */
    @NotNull(message = "{msg.repair.entryDate.notNull}")
    private LocalDate entryDate;

    /**
     * ID de la cita que origina esta reparación.
     */
    @NotNull(message = "{msg.repair.appointment.notNull}")
    private Long appointmentId;

    /**
     * ID del vehículo a reparar.
     */
    @NotNull(message = "{msg.repair.vehicle.notNull}")
    private Long vehicleId;

    /**
     * Comentarios iniciales sobre el estado del vehículo.
     */
    private String notes;
}