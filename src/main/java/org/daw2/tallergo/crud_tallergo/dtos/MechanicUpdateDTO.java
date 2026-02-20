package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la actualización de un mecánico existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MechanicUpdateDTO {

    @NotNull(message = "{msg.mechanic.id.notNull}")
    private Long id;

    @NotBlank(message = "{msg.mechanic.name.notEmpty}")
    @Size(max = 100, message = "{msg.mechanic.name.size}")
    private String name;

    @NotBlank(message = "{msg.mechanic.specialty.notEmpty}")
    @Size(max = 50, message = "{msg.mechanic.specialty.size}")
    private String specialty;

    @NotNull(message = "{msg.mechanic.workshop.notNull}")
    private Integer workshopId;
}