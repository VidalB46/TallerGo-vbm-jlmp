package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO básico para listado de mecánicos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MecanicoDTO {

    private Long id;
    private String nombre;
    private String especialidad;
    private String nombreTaller;

    // Cambiamos para que coincida con el mapper
    private Long tallerId;
    private String tallerNombre;
}