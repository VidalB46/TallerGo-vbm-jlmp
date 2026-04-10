package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * DTO para la solicitud de una nueva cita por parte del usuario.
 * Encapsula los datos necesarios para programar una intervención en un taller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCreateDTO {

    /**
     * Fecha y hora de inicio deseada para la cita.
     */
    @NotNull(message = "{msg.appointment.startDate.notNull}")
    private LocalDateTime startDate;

    /**
     * Notas o descripción del problema detectado en el vehículo.
     */
    @Size(max = 1000, message = "{msg.appointment.notes.size}")
    private String notes;

    /**
     * ID del vehículo asociado a la cita.
     */
    @NotNull(message = "{msg.appointment.vehicle.notNull}")
    private Long vehicleId;

    /**
     * ID del taller seleccionado para la cita.
     */
    @NotNull(message = "{msg.appointment.workshop.notNull}")
    private Integer workshopId;

    /**
     * Fichero de imagen subido por el cliente (opcional).
     */
    private MultipartFile mediaFile;

    /**
     * Ruta web de la imagen generada tras guardarse en el disco duro.
     */
    private String mediaUrl;
}