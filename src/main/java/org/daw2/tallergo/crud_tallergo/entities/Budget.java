package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidad JPA que representa un presupuesto asociado a una reparación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "repair")
@ToString(exclude = "repair")
@Entity
@Table(name = "budgets")
public class Budget {

    /**
     * Identificador único del presupuesto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Importe total bruto del presupuesto.
     */
    @Column(name = "total_gross", precision = 10, scale = 2)
    private BigDecimal totalGross;

    /**
     * Importe total neto del presupuesto.
     */
    @Column(name = "total_net", precision = 10, scale = 2)
    private BigDecimal totalNet;

    /**
     * Reparación a la que pertenece este presupuesto.
     * Mantiene la clave foránea en la tabla 'budgets'.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_id", nullable = false, unique = true)
    private Repair repair;
}