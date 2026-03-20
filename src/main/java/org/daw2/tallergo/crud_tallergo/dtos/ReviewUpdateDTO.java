package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO para que un usuario pueda modificar su valoración original.
 */
@Data
public class ReviewUpdateDTO {

    @NotNull(message = "{msg.review.id.notNull}")
    private Long id;

    @Min(1) @Max(5)
    private Integer rating;

    private String comment;
}