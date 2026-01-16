package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;

import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla 'appointments'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"repair", "user", "workshop", "vehicle"})
@ToString(exclude = {"repair", "user", "workshop", "vehicle"})
@Entity
@Table(name = "appointments")
public class Appointment {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** DATETIME NOT NULL */
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    /** DATETIME NULL */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    /** ENUM('SOLICITADO', 'CONFIRMADO', 'CANCELADO') */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status = AppointmentStatus.SOLICITADO;

    /** TEXT NULL */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /** VARCHAR(255) NULL */
    @Column(name = "media_url", length = 255)
    private String mediaUrl;

    /** Relación N:1 con User (Cliente) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Relación N:1 con Workshop (Taller) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    /** Relación N:1 con Vehicle */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /** Relación 1:1 con Repair */
    @OneToOne(mappedBy = "appointment", fetch = FetchType.LAZY)
    private Repair repair;
}
