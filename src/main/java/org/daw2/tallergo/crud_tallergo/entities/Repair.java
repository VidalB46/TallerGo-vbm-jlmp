package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RepairStatus status = RepairStatus.STANDBY;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL)
    @OrderBy("id DESC")
    private List<Budget> budgets = new ArrayList<>();

    /**
     * Devuelve el último presupuesto activo (no rechazado).
     */
    public Budget getBudget() {
        if (budgets == null || budgets.isEmpty()) {
            return null;
        }

        return budgets.stream()
                .filter(b -> Boolean.FALSE.equals(b.getRejected()))
                .max(Comparator.comparing(Budget::getId))
                .orElse(null);
    }
}