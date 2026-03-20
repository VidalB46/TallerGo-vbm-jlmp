package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.Data;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;
import java.time.LocalDate;

/**
 * DTO para la visualización de datos de una reparación en curso o finalizada.
 */
@Data
public class RepairDTO {
    private Long id;
    private LocalDate entryDate;
    private RepairStatus status;
    private String vehicleMatricula;
    private String vehicleModel;
    private Long appointmentId;
}