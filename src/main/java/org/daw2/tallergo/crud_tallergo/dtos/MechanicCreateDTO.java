package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Data Transfer Object (DTO) utilizado exclusivamente para la creación de un nuevo Mecánico.
 * <p>
 * A diferencia de los DTOs de actualización o detalle, este objeto no incluye
 * un identificador ({@code id}), ya que este será generado automáticamente por
 * la base de datos al persistir la entidad.
 * </p>
 * <p>
 * Incluye validaciones de Jakarta (Bean Validation) con mensajes internacionalizados
 * para asegurar que los datos obligatorios, como el nombre, la especialidad y
 * el taller asignado, estén presentes antes de procesar la petición.
 * </p>
 */
@Data
public class MechanicCreateDTO {

    /**
     * Nombre completo del mecánico.
     * Se valida que no sea nulo ni esté en blanco ({@code @NotBlank}).
     */
    @NotBlank(message = "{msg.mechanic.name.notBlank}")
    private String name;

    /**
     * Especialidad técnica del mecánico (por ejemplo: "Electricidad", "Chapa y Pintura", "Motor").
     * Es un campo obligatorio para conocer el perfil del trabajador.
     */
    @NotBlank(message = "{msg.mechanic.specialty.notBlank}")
    private String specialty;

    /**
     * Identificador del taller (Workshop) al que estará asociado este mecánico.
     * Es estrictamente necesario ({@code @NotNull}) para establecer la relación
     * entre el mecánico y su lugar de trabajo en la base de datos.
     */
    @NotNull(message = "{msg.mechanic.workshop.notNull}")
    private Integer workshopId;
}