package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un presupuesto asociado a una reparación.
 * Soporta versionado: una misma reparación puede tener varios presupuestos si
 * el taller añade modificaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"repair", "lines"})
@ToString(exclude = {"repair", "lines"})
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Importe total sin impuestos.
     */
    @Column(name = "total_gross", precision = 10, scale = 2)
    private BigDecimal totalGross;

    /**
     * Importe total incluyendo el IVA aplicado.
     */
    @Column(name = "total_net", precision = 10, scale = 2)
    private BigDecimal totalNet;

    /**
     * Indica si el cliente ha aprobado formalmente el presupuesto.
     */
    @Column(name = "accepted", nullable = false)
    private Boolean accepted = false;

    /**
     * Indica si el presupuesto ha sido rechazado (ej. una modificación no aceptada).
     * Este campo permite mantener el historial sin borrar datos.
     */
    @Column(name = "rejected", nullable = false)
    private Boolean rejected = false;

    /**
     * Observaciones o comentarios adicionales del mecánico para el cliente.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Relación con la reparación. Cambiada a ManyToOne para permitir
     * el histórico de versiones (v1, v2, etc.).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_id", nullable = false)
    private Repair repair;

    /**
     * Colección de conceptos detallados (piezas, mano de obra, etc.) que componen el presupuesto.
     */
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetLine> lines = new ArrayList<>();

    /**
     * Método  para añadir líneas de presupuesto.
     * @param line La línea de presupuesto a añadir.
     */
    public void addLine(BudgetLine line) {
        lines.add(line);
        line.setBudget(this);
    }
}