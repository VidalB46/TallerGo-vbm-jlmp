package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para que el cliente pueda valorar un taller tras una reparación.
 */
@Data
public class ReviewCreateDTO {

    /**
     * Puntuación del 1 al 5.
     */
    @NotNull
    @Min(1) @Max(5)
    private Integer rating;

    /**
     * Comentario opcional sobre la experiencia.
     */
    @NotBlank
    private String comment;

    /**
     * Taller al que se puntúa.
     */
    @NotNull
    private Integer workshopId;
}