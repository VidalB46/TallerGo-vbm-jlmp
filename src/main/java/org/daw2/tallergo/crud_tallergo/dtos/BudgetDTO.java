package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO que representa la información económica de una reparación.
 */
@Data
public class BudgetDTO {
    private Long id;
    private BigDecimal totalGross; // Importe Bruto
    private BigDecimal totalNet;   // Importe Neto (con impuestos/descuentos)
    private Long repairId;
    private boolean accepted;
}