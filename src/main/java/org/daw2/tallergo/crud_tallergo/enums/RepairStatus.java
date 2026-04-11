package org.daw2.tallergo.crud_tallergo.enums;

/**
 * Enumeración que define las etapas del ciclo de vida de una reparación en el taller.
 * Actualizado para la Fase 2 (Presupuestos previos a la recepción física).
 */
public enum RepairStatus {

    /** * El expediente de reparación ha sido creado (Cita Confirmada),
     * pero el vehículo está en espera. Aplica tanto si el coche no ha llegado físicamente
     * como si está aparcado esperando a que un mecánico se libere.
     */
    STANDBY("En espera"),

    /** * El mecánico está trabajando actualmente en el vehículo tras aceptar el presupuesto.
     */
    ACTIVO("En reparación"),

    /** * La reparación ha concluido y el vehículo está listo para ser entregado al cliente.
     */
    FINALIZADO("Listo para recoger");

    /**
     * Nombre legible y amigable para mostrar en las vistas (Thymeleaf).
     */
    private final String displayName;

    /**
     * Constructor de la enumeración.
     * @param displayName Nombre a mostrar en la interfaz de usuario.
     */
    RepairStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Obtiene el nombre formateado del estado.
     * @return Cadena de texto con el estado (ej: "En espera").
     */
    public String getDisplayName() {
        return displayName;
    }
}