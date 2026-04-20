package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para la visualización detallada del presupuesto por parte del cliente o el administrador.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDetailDTO {
    private Long id;
    private BigDecimal totalGross;
    private BigDecimal totalNet;
    private Boolean accepted;
    private String notes; // Observaciones del taller
    private Long repairId;
    private String vehicleMatricula;
    private List<BudgetLineDTO> lines;
}