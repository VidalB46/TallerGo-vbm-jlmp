package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un presupuesto asociado a una reparación.
 * Soporta versionado: una misma reparación puede tener varios presupuestos
 * si el taller añade modificaciones.
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

    @Column(name = "total_gross", precision = 10, scale = 2)
    private BigDecimal totalGross;

    @Column(name = "total_net", precision = 10, scale = 2)
    private BigDecimal totalNet;

    @Column(name = "accepted", nullable = false)
    private Boolean accepted = false;

    @Column(name = "rejected", nullable = false)
    private Boolean rejected = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_id", nullable = false)
    private Repair repair;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetLine> lines = new ArrayList<>();

    public void addLine(BudgetLine line) {
        lines.add(line);
        line.setBudget(this);
    }
}