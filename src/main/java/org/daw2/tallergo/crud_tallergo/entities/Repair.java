package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator; // IMPORTANTE
import java.util.List;

/**
 * Entidad JPA que representa un expediente u orden de reparación en el taller.
 * Con el nuevo flujo de tele-peritación, esta entidad se crea automáticamente
 * al confirmarse la cita, permitiendo presupuestar antes de que el coche llegue.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"appointment", "budgets", "vehicle"})
@ToString(exclude = {"appointment", "budgets", "vehicle"})
@Entity
@Table(name = "repairs")
public class Repair {

    /**
     * Identificador único de la orden de reparación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha de entrada real del vehículo al taller.
     * Será nula al crearse el expediente y se rellenará automáticamente
     * cuando el Admin pulse en "Recepcionar Vehículo".
     */
    @Column(name = "entry_date")
    private LocalDate entryDate;

    /**
     * Estado actual del proceso de reparación.
     * Por defecto inicia en STANDBY (En espera).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RepairStatus status = RepairStatus.STANDBY;

    /**
     * Notas técnicas o comentarios detallados del mecánico durante su trabajo.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Cita previa que originó esta reparación.
     * Relación obligatoria 1:1.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    /**
     * Vehículo objeto de la reparación, heredado automáticamente de la Cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /**
     * Historial de presupuestos asociados a esta reparación.
     */
    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL)
    @OrderBy("id DESC")
    private List<Budget> budgets = new ArrayList<>();

    /**
     * Filtra el historial y busca EXPLICITAMENTE el que tenga el ID más alto
     * para asegurar que siempre cogemos la última versión (v2, v3...).
     *
     * @return El presupuesto activo de la reparación, o null si no hay ninguno.
     */
    public Budget getBudget() {
        if (budgets == null || budgets.isEmpty()) {
            return null;
        }
        return budgets.stream()
                .filter(b -> b.getRejected() == null || !b.getRejected()) // Que no esté rechazado
                .max(Comparator.comparing(Budget::getId)) // que busque el ID más alto
                .orElse(null);
    }
}