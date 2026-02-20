package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un mecánico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MecanicoCreateDTO {

    private Long id;


    @NotEmpty(message = "{msg.mecanico.nombre.notEmpty}")
    @Size(max = 50, message = "{msg.mecanico.nombre.size}")
    private String nombre;

    @NotEmpty(message = "{msg.mecanico.especialidad.notEmpty}")
    @Size(max = 50, message = "{msg.mecanico.especialidad.size}")
    private String especialidad;

    @NotNull(message = "{msg.mecanico.taller.notNull}")
    private Long tallerId;
}