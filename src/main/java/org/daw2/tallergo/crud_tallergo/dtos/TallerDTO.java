package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * DTO completo para Taller
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TallerDTO {

    private Long id;

    @NotEmpty(message = "{msg.taller.nombre.notEmpty}")
    @Size(max = 100, message = "{msg.taller.nombre.size}")
    private String nombre;

    @NotEmpty(message = "{msg.taller.direccion.notEmpty}")
    @Size(max = 200, message = "{msg.taller.direccion.size}")
    private String direccion;
}