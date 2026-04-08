package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import java.time.LocalDateTime;

/**
 * DTO que representa la información completa de una cita.
 * Incluye datos extendidos del vehículo, el taller y el cliente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetailDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private AppointmentStatus status;
    private String notes;
    private String mediaUrl;

    // Objetos DTO relacionados para mostrar información completa en la vista de detalle
    private VehicleDTO vehicle;
    private WorkshopDTO workshop;
    private String userEmail;

    // Identificadores y banderas de la reparación asociada
    private Long repairId;
    private Boolean hasBudget;
    private Boolean isBudgetAccepted; // Controla si se puede iniciar la reparación
}