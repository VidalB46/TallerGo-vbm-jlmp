package org.daw2.tallergo.crud_tallergo.exceptions;

/**
 * Excepción genérica que indica que no se ha encontrado un recurso en la aplicación.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resource;
    private final String field;
    private final Object value;

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(resource + " not found (" + field + " =" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

}