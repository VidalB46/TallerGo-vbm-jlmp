package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.Data;

/**
 * Data Transfer Object (DTO) ligero que representa la información básica de un Mecánico.
 * <p>
 * A diferencia de {@code MechanicDetailDTO}, este objeto utiliza una estrategia de
 * "aplanamiento" (flattening), incluyendo únicamente el nombre del taller ({@code workshopName})
 * en lugar del objeto taller completo. Es la estructura ideal para devolver listados o tablas
 * de mecánicos donde solo se requiere información resumida, optimizando así el tamaño de
 * la respuesta JSON y el rendimiento de la red.
 * </p>
 */
@Data
public class MechanicDTO {

    /**
     * Identificador único del mecánico.
     */
    private Long id;

    /**
     * Nombre completo del mecánico.
     */
    private String name;

    /**
     * Especialidad técnica del mecánico (por ejemplo: "Electricidad", "Neumáticos").
     */
    private String specialty;

    /**
     * Nombre del taller al que pertenece el mecánico.
     */
    private String workshopName;
}