package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para la creación de un nuevo presupuesto asociado a una reparación.
 * Se utiliza cuando el mecánico evalúa la avería y emite una propuesta económica inicial.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCreateDTO {

    /**
     * ID de la reparación a la que pertenece este presupuesto.
     * Es estrictamente obligatorio para vincular el dinero con el trabajo.
     */
    @NotNull(message = "{msg.budget.repair.notNull}")
    private Long repairId;

    /**
     * Importe total bruto estimado.
     * Se valida que no sea nulo y que no sea un valor negativo.
     */
    @NotNull(message = "{msg.budget.totalGross.notNull}")
    @PositiveOrZero(message = "{msg.budget.totalGross.positive}")
    private BigDecimal totalGross;

    /**
     * Importe total neto estimado (aplicando impuestos o descuentos).
     * Se valida que no sea nulo y que no sea un valor negativo.
     */
    @NotNull(message = "{msg.budget.totalNet.notNull}")
    @PositiveOrZero(message = "{msg.budget.totalNet.positive}")
    private BigDecimal totalNet;

    // Nota: No incluimos el campo 'accepted' aquí porque, por pura lógica de negocio,
    // cuando un mecánico CREA un presupuesto, este nace siempre en estado "No aceptado" (false).
}