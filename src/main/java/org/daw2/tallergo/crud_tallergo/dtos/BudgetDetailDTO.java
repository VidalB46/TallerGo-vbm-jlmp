package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO para visualizar la información económica detallada de una reparación.
 * Se utiliza en la vista de cliente para que este tome la decisión de aceptar el trabajo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDetailDTO {
    private Long id;
    private BigDecimal totalGross; // Bruto
    private BigDecimal totalNet;   // Neto (con IVA/descuentos)
    private Boolean accepted;      // Estado de la aprobación

    // Información simplificada de la reparación asociada
    private Long repairId;
    private String vehicleMatricula;
}