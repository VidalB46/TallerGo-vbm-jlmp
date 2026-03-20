package org.daw2.tallergo.crud_tallergo.exceptions;

import lombok.Getter;

/**
 * Excepción personalizada para indicar que un recurso solicitado no existe en el sistema.
 * Se lanza habitualmente en la capa de servicios cuando una búsqueda por ID o por un campo
 * único no devuelve resultados.
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    /** Nombre del recurso que no se pudo localizar (ej: "User", "Appointment"). */
    private final String resource;

    /** Nombre del criterio de búsqueda utilizado (ej: "id", "email"). */
    private final String field;

    /** El valor específico que se intentó buscar. */
    private final Object value;

    /**
     * Construye una nueva excepción con un mensaje formateado con los detalles del recurso.
     *
     * @param resource Nombre de la entidad.
     * @param field    Nombre del atributo de búsqueda.
     * @param value    Valor que no produjo resultados.
     */
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found (%s = %s)", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

}