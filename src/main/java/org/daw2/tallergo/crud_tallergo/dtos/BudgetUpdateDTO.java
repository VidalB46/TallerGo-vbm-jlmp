package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO utilizado para la gestión del presupuesto.
 * Permite al mecánico ajustar importes o al cliente marcarlo como aceptado.
 */
@Data
public class BudgetUpdateDTO {

    @NotNull(message = "{msg.budget.id.notNull}")
    private Long id;

    private BigDecimal totalGross;
    private BigDecimal totalNet;

    /**
     * Campo crítico: permite registrar si el cliente ha dado el visto bueno.
     */
    @NotNull(message = "{msg.budget.accepted.notNull}")
    private Boolean accepted;
}