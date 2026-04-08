package org.daw2.tallergo.crud_tallergo.enums;

/**
 * Enumeración que define los posibles estados de una cita en el sistema.
 */
public enum AppointmentStatus {
    /** El usuario ha solicitado la cita pero el taller aún no la ha validado. */
    SOLICITADO("Pendiente de confirmación"),

    /** El taller ha aceptado la cita y se ha asignado un hueco en el calendario. */
    CONFIRMADO("Confirmada"),

    /** La cita ha sido anulada por el usuario o rechazada por el taller. */
    CANCELADO("Cancelada"),

    /** El vehículo ya ha sido recepcionado por el mecánico. */
    EN_TALLER("Vehículo en el taller"),

    /** El mecánico ha pulsado "Comenzar Trabajo". */
    EN_REPARACION("En reparación"),

    /** El mecánico ha pulsado "Finalizar Reparación". */
    LISTO_RECOGIDA("Listo para recoger"),

    /** El mecánico ha entregado las llaves al cliente. */
    RECOGIDO("Vehículo recogido");

    private final String displayName;

    AppointmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}