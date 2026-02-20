package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MechanicCreateDTO {
    @NotBlank(message = "{msg.mechanic.name.notBlank}")
    private String name;

    @NotBlank(message = "{msg.mechanic.specialty.notBlank}")
    private String specialty;

    @NotNull(message = "{msg.mechanic.workshop.notNull}")
    private Integer workshopId;
}