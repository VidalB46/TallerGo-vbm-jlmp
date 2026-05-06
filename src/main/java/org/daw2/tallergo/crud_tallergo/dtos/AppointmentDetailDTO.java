package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import java.time.LocalDateTime;

/**
 * DTO de detalle para una cita ({@link org.daw2.tallergo.crud_tallergo.entities.Appointment}).
 * Contiene toda la información de la cita junto con el vehículo, taller y estado del presupuesto asociado.
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

    private Boolean isDateAcceptedByClient;

    private VehicleDTO vehicle;
    private WorkshopDTO workshop;
    private String userEmail;

    private Long repairId;
    private Boolean hasBudget;
    private Boolean isBudgetAccepted;
}