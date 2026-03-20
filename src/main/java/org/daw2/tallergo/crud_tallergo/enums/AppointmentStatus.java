package org.daw2.tallergo.crud_tallergo.enums;

/**
 * Enumeración que define los posibles estados de una cita en el sistema.
 */
public enum AppointmentStatus {
    /** * El usuario ha solicitado la cita pero el taller aún no la ha validado.
     */
    SOLICITADO,

    /** * El taller ha aceptado la cita y se ha asignado un hueco en el calendario.
     */
    CONFIRMADO,

    /** * La cita ha sido anulada por el usuario o rechazada por el taller.
     */
    CANCELADO
}