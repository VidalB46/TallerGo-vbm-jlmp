package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;

import java.time.LocalDate;

/**
 * Entidad JPA para la tabla 'repairs'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointment", "budget", "vehicle"})
@ToString(exclude = {"appointment", "budget", "vehicle"})
@Entity
@Table(name = "repairs")
public class Repair {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** DATE NULL - Fecha de entrada real */
    @Column(name = "entry_date")
    private LocalDate entryDate;

    /** ENUM STANDBY/ACTIVO/FINALIZADO */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RepairStatus status = RepairStatus.STANDBY;

    /** TEXT NULL - Notas del mecánico */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /** Relación 1:1 con Appointment (Dueña de la FK) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    /** Relación N:1 con Vehicle (Redundancia útil) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /** Relación 1:1 con Budget */
    @OneToOne(mappedBy = "repair", fetch = FetchType.LAZY)
    private Budget budget;
}