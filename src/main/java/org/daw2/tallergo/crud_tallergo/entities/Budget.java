package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un presupuesto asociado a una reparación.
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
     * Importe total SIN IVA (La suma de todas las líneas)
     */
    @Column(name = "total_gross", precision = 10, scale = 2)
    private BigDecimal totalGross;

    /**
     * Importe total CON IVA (Lo que paga el cliente)
     */
    @Column(name = "total_net", precision = 10, scale = 2)
    private BigDecimal totalNet;

    @Column(name = "accepted", nullable = false)
    private Boolean accepted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_id", nullable = false, unique = true)
    private Repair repair;

    /**
     * cascade = ALL significa que si guardamos el presupuesto, sus líneas se guardan solas.
     * orphanRemoval = true significa que si borramos una línea de la lista, se borra de la BBDD.
     */
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetLine> lines = new ArrayList<>();

    /**
     * Helper para añadir líneas fácilmente y mantener la relación bidireccional
     */
    public void addLine(BudgetLine line) {
        lines.add(line);
        line.setBudget(this);
    }
}