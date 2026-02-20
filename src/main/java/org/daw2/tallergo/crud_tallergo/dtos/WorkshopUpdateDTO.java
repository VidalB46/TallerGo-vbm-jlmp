package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkshopUpdateDTO extends WorkshopCreateDTO {
    @NotNull(message = "{msg.workshop.id.notNull}")
    private Integer id;
}