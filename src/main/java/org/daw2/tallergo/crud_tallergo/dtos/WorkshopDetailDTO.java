package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.Data;
import java.util.List;

/**
 * DTO para visualizar los detalles completos de un taller.
 */
@Data
public class WorkshopDetailDTO {

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
     * Horario de apertura del taller.
     */
    private String schedule;

    /**
     * Correo electrónico de contacto del taller.
     */
    private String email;

    /**
     * Teléfono de contacto del taller.
     */
    private String phone;

    /**
     * Lista de mecánicos asociados al taller.
     */
    private List<MechanicDTO> mechanics;


    private Double averageRating;
}