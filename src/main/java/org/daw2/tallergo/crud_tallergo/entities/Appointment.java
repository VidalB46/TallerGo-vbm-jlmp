package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una cita en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"repair", "user", "workshop", "vehicle"})
@ToString(exclude = {"repair", "user", "workshop", "vehicle"})
@Entity
@Table(name = "appointments")
public class Appointment {

    /**
     * Identificador único de la cita.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha y hora de inicio de la cita.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /**
     * Fecha y hora de finalización de la cita.
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    /**
     * Estado actual de la cita.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.SOLICITADO;

    /**
     * Notas o comentarios adicionales sobre la cita.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * URL de un archivo multimedia adjunto a la cita.
     */
    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    /**
     * Usuario (cliente) que solicita la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Taller en el que se ha programado la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    /**
     * Vehículo asociado a la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /**
     * Reparación generada a partir de la cita, si la hubiera.
     */
    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY)
    private Repair repair;
}