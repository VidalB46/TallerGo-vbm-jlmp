package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "{msg.workshop.phone.notBlank}")
    @Size(max = 20)
    private String phone;

    /**
     * Ubicación o dirección del taller.
     */
    @NotBlank(message = "{msg.workshop.location.notBlank}")
    @Size(max = 255)
    private String location;

    /**
     * Correo electrónico del taller.
     * Puede venir vacío, pero si se informa debe tener formato válido.
     */
    @Size(max = 100)
    @Pattern(
            regexp = "^$|^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$",
            message = "{msg.workshop.email.invalid}"
    )
    private String email;

    /**
     * Horario de apertura del taller.
     */
    @NotBlank(message = "{msg.workshop.schedule.notBlank}")
    @Size(max = 100)
    private String schedule;
}