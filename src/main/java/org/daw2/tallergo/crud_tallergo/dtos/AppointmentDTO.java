package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import java.time.LocalDateTime;

/**
 * DTO simplificado para listados de citas.
 * Proporciona información básica para vistas de agenda o historial.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {

    private Long id;
    private LocalDateTime startDate;
    private AppointmentStatus status;
    private String vehicleModel;     // Para mostrar el nombre del coche sin cargar toda la entidad
    private String workshopName;     // Para mostrar el nombre del taller
    private String userEmail;        // Identificador del cliente
    private Boolean hasBudget;
    private Boolean isBudgetAccepted;
}