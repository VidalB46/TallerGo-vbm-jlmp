package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para la solicitud de creación de un presupuesto.
 * Solo recibe lo que introduce el usuario (Conceptos, cantidades, precio unitario y notas).
 * Los totales (Bruto y Neto) los calcula el backend automáticamente.
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
    private List<BudgetLineDTO> lines = new ArrayList<>();
}