package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidad JPA para la tabla 'budgets'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "repair")
@ToString(exclude = "repair")
@Entity
@Table(name = "budgets")
public class Budget {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** DECIMAL(10,2) NULL */
    @Column(name = "total_gross", precision = 10, scale = 2)
    private BigDecimal totalGross;

    /** DECIMAL(10,2) NULL */
    @Column(name = "total_net", precision = 10, scale = 2)
    private BigDecimal totalNet;

    /** Relación 1:1 con Repair (Dueña de la FK) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_id", nullable = false, unique = true)
    private Repair repair;
}
