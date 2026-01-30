package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO detallado de mecánico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MecanicoDetailDTO {


    private Long id;
    private String nombre;
    private String especialidad;

    private Long tallerId;
    private String tallerNombre;
    private String tallerDireccion;
}