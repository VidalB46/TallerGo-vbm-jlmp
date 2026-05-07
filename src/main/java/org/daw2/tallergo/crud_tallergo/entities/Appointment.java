package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una cita en el taller.
 * Relaciona a un {@link User} con un {@link Workshop} y un {@link Vehicle},
 * y puede derivar en una {@link Repair} una vez confirmada.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"repair", "user", "workshop", "vehicle"})
@ToString(exclude = {"repair", "user", "workshop", "vehicle"})
@Entity
@Table(name = "appointments")
public class Appointment {

    /** Identificador único de la cita. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora de inicio de la cita. */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /** Fecha y hora de fin de la cita. */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    /** Estado actual de la cita (p. ej. {@code SOLICITADO}, {@code CONFIRMADO}). */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.SOLICITADO;

    /** Notas u observaciones adicionales sobre la cita. */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /** URL del medio adjunto a la cita (foto, vídeo, etc.). */
    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    /** Indica si el cliente ha aceptado la fecha propuesta para la cita. */
    // Control de aceptación del cliente
    @Column(name = "is_date_accepted_by_client", nullable = false)
    private Boolean isDateAcceptedByClient = true;

    /** Usuario propietario del vehículo que solicita la cita. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Taller en el que se realizará la cita. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    /** Vehículo para el que se solicita la cita. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /** Reparación derivada de esta cita, si existe. */
    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Repair repair;

    @Column(name = "archived_by_client")
    private boolean archivedByClient = false;
}