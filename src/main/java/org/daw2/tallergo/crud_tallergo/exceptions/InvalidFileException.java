package org.daw2.tallergo.crud_tallergo.exceptions;

public class InvalidFileException extends RuntimeException {

    private final String resource;
    private final String field;
    private final Object value;

    public InvalidFileException(String resource, String field, Object value) {
        super("Invalid file for " + resource + " (" + field + " =" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public InvalidFileException(String resource, String field, Object value, String detail) {
        super("Invalid file for " + resource + " (" + field + " =" + value + "): " + detail);
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

}