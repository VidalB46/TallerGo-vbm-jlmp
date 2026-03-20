package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para la lectura de datos de un taller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkshopDTO {

    /**
     * Identificador único del taller.
     */
    private Integer id;

    /**
     * NIF o CIF del taller.
     */
    private String nif;

    /**
     * Nombre del taller.
     */
    private String name;

    /**
     * Ubicación o dirección del taller.
     */
    private String location;

    /**
     * Teléfono de contacto del taller.
     */
    private String phone;
}