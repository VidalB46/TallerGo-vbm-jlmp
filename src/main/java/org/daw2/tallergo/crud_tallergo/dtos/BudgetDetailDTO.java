package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para visualizar la información económica detallada de una reparación.
 * Se utiliza en la vista de cliente para mostrar el desglose de conceptos y totales.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDetailDTO {

    private Long id;

    private BigDecimal totalGross; // Bruto (Sin IVA)

    private BigDecimal totalNet;   // Neto (Con IVA)

    private Boolean accepted;      // Estado de la aprobación

    private Long repairId;

    private String vehicleMatricula;

    /**
     * Lista de líneas con el desglose de piezas y mano de obra.
     */
    private List<BudgetLineDTO> lines;
}