package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para la transferencia de datos de una línea individual del presupuesto.
 * Contiene el concepto, la cantidad y el precio unitario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetLineDTO {

    private Long id;

    /**
     * Descripción del servicio o pieza (Ej: "Filtro de aceite").
     */
    @NotBlank(message = "El concepto es obligatorio")
    private String concept;

    /**
     * Número de unidades o horas de trabajo.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer quantity;

    /**
     * Precio por unidad sin impuestos.
     */
    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private BigDecimal unitPrice;

    /**
     * Total calculado de la línea (Cantidad * Precio Unitario).
     * Útil para mostrar en la vista del cliente.
     */
    private BigDecimal lineTotal;
}