package org.daw2.tallergo.crud_tallergo.exceptions;

/**
 * Excepción genérica que indica que ya existe un recurso con un valor que debería ser único.
 */
public class DuplicateResourceException extends RuntimeException {

    private final String resource;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String resource, String field, Object value) {
        super("Duplicate " + resource + " (" + field + " =" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

}