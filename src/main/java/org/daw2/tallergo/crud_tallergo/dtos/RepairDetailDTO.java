package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.Data;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;
import java.time.LocalDate;

/**
 * DTO para visualizar el expediente completo de una reparación.
 * Incluye la información de la cita origen y el presupuesto si existe.
 */
@Data
public class RepairDetailDTO {
    private Long id;
    private LocalDate entryDate;
    private RepairStatus status;
    private String notes;

    private VehicleDTO vehicle;
    private AppointmentDTO appointment;
    private BudgetDTO budget; // Para mostrar el dinero en la misma pantalla
}