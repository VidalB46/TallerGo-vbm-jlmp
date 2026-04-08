package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para la creación de reseñas.
 */
@Data
public class ReviewCreateDTO {

    @NotNull(message = "{validation.rating.notnull}")
    @Min(1) @Max(5)
    private Integer rating;

    @NotBlank(message = "{validation.comment.notblank}")
    private String comment;

    @NotNull
    private Integer workshopId;

    private Long userId;
}