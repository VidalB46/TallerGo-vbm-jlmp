package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidad JPA que representa una línea de concepto dentro de un presupuesto.
 * Ej: "Cambio de aceite", Cantidad: 1, Precio: 45.00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "budget")
@ToString(exclude = "budget")
@Entity
@Table(name = "budget_lines")
public class BudgetLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descripción del servicio o pieza.
     */
    @Column(name = "concept", nullable = false)
    private String concept;

    /**
     * Cantidad (ej: 2 horas de mano de obra, 4 neumáticos).
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Precio unitario de la pieza o servicio.
     */
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    /**
     * El presupuesto general al que pertenece esta línea.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    /**
     * Método helper para calcular el total de esta línea (Cantidad * Precio Unitario)
     */
    public BigDecimal getLineTotal() {
        if (unitPrice == null || quantity == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}