package org.daw2.tallergo.crud_tallergo.exceptions;

/**
 * Excepción genérica que indica que ya existe un recurso con un valor que debería ser único.
 */
public class DuplicateResourceException extends RuntimeException {

    /** Nombre del recurso (entidad) en el que se detectó la duplicidad. */
    private final String resource;

    /** Campo del recurso cuyo valor está duplicado. */
    private final String field;

    /** Valor duplicado que generó el conflicto. */
    private final Object value;

    /**
     * Crea una nueva excepción de recurso duplicado.
     *
     * @param resource Nombre del recurso (p. ej. "User", "Workshop").
     * @param field    Nombre del campo con el valor duplicado (p. ej. "email").
     * @param value    Valor concreto que causó la duplicidad.
     */
    public DuplicateResourceException(String resource, String field, Object value) {
        super("Duplicate " + resource + " (" + field + " =" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

}