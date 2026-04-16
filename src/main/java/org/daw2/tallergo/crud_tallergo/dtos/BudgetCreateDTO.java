package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para la creación de un nuevo presupuesto asociado a una reparación.
 * El mecánico enviará una lista de conceptos y el servidor calculará los totales automáticamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCreateDTO {

    /**
     * ID de la reparación a la que pertenece este presupuesto.
     */
    @NotNull(message = "{msg.budget.repair.notNull}")
    private Long repairId;

    @NotNull(message = "{msg.budget.totalGross.notNull}")
    private BigDecimal totalGross;

    @NotNull(message = "{msg.budget.totalNet.notNull}")
    private BigDecimal totalNet;

    /**
     * Lista de conceptos a presupuestar.
     * Se exige al menos una línea para que el presupuesto sea válido.
     */
    @Valid
    @Size(min = 1, message = "El presupuesto debe tener al menos una línea de concepto")
    private List<BudgetLineDTO> lines = new ArrayList<>();
}