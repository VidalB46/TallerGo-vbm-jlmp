package org.daw2.tallergo.crud_tallergo.exceptions;

import lombok.Getter;

/**
 * Excepción lanzada cuando un archivo proporcionado no cumple con los requisitos
 * de formato, tamaño o integridad esperados por el sistema.
 */
@Getter
public class InvalidFileException extends RuntimeException {

    /** Nombre del recurso que esperaba el archivo (ej: "UserProfile", "Appointment"). */
    private final String resource;

    /** Nombre del campo asociado al archivo (ej: "profileImage", "mediaUrl"). */
    private final String field;

    /** Identificador o nombre del archivo que causó el error. */
    private final Object value;

    /**
     * Construye una excepción con un mensaje estándar de archivo inválido.
     *
     * @param resource Entidad afectada.
     * @param field    Campo de la entidad.
     * @param value    Valor o nombre del archivo.
     */
    public InvalidFileException(String resource, String field, Object value) {
        super(String.format("Invalid file for %s (%s = %s)", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    /**
     * Construye una excepción incluyendo detalles adicionales sobre el motivo del error.
     *
     * @param resource Entidad afectada.
     * @param field    Campo de la entidad.
     * @param value    Valor o nombre del archivo.
     * @param detail   Explicación detallada (ej: "MIME type not supported").
     */
    public InvalidFileException(String resource, String field, Object value, String detail) {
        super(String.format("Invalid file for %s (%s = %s): %s", resource, field, value, detail));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}