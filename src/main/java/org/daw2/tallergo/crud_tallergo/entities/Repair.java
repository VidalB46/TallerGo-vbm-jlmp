package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;

import java.time.LocalDate;

/**
 * Entidad JPA que representa una orden de reparación en el taller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointment", "budget", "vehicle"})
@ToString(exclude = {"appointment", "budget", "vehicle"})
@Entity
@Table(name = "repairs")
public class Repair {

    /**
     * Identificador único de la reparación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha de entrada real del vehículo al taller.
     */
    @Column(name = "entry_date")
    private LocalDate entryDate;

    /**
     * Estado actual del proceso de reparación.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RepairStatus status = RepairStatus.STANDBY;

    /**
     * Notas técnicas o comentarios detallados del mecánico.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Cita previa que originó esta reparación.
     * Mantiene la clave foránea en la tabla 'repairs'.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    /**
     * Vehículo objeto de la reparación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /**
     * Presupuesto asociado a esta reparación concreta.
     */
    @OneToOne(mappedBy = "repair", fetch = FetchType.LAZY)
    private Budget budget;
}