package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) que representa los detalles completos de un Mecánico.
 * <p>
 * A diferencia de los DTOs simples o de creación, esta clase incluye no solo
 * la información básica del mecánico, sino también el objeto anidado {@link WorkshopDTO}.
 * Esto permite enviar al cliente (frontend o App) toda la información del trabajador
 * junto con los datos del taller al que pertenece en una sola respuesta.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MechanicDetailDTO {

    /**
     * Identificador único del mecánico.
     */
    private Long id;

    /**
     * Nombre completo del mecánico.
     */
    private String name;

    /**
     * Especialidad técnica del mecánico (por ejemplo: "Electricidad", "Chapa y Pintura", "Mecánica rápida").
     */
    private String specialty;

    /**
     * Detalles del taller al que está asignado el mecánico.
     * Al ser un DTO anidado, proporciona el contexto completo del lugar de trabajo
     * sin exponer la entidad original de base de datos.
     */
    private WorkshopDTO workshop;
}