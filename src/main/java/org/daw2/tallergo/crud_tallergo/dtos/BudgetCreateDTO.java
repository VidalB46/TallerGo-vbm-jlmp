package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para la solicitud de creación de un presupuesto.
 * Incluye la referencia a la reparación, los conceptos detallados y las notas explicativas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetCreateDTO {

    @NotNull(message = "{msg.budget.repair.notNull}")
    private Long repairId;

    /**
     * Comentarios del taller justificando los trabajos o piezas presupuestadas.
     */
    private String notes;

    @Valid
    @Size(min = 1, message = "El presupuesto debe tener al menos una línea de concepto")
    private List<BudgetLineDTO> lines = new ArrayList<>();
}