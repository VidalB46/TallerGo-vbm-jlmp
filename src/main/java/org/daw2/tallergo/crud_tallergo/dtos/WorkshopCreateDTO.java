package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para la creación de un taller.
 */
@Data
public class WorkshopCreateDTO {

    /**
     * NIF o CIF del taller.
     */
    @NotBlank(message = "{msg.workshop.nif.notBlank}")
    @Size(max = 20)
    private String nif;

    /**
     * Nombre del taller.
     */
    @NotBlank(message = "{msg.workshop.name.notBlank}")
    @Size(max = 150)
    private String name;

    /**
     * Teléfono de contacto del taller.
     */
    private String phone;

    /**
     * Ubicación o dirección del taller.
     */
    private String location;

    /**
     * Correo electrónico del taller.
     */
    private String email;

    /**
     * Horario de apertura del taller.
     */
    private String schedule;
}