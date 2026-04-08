package org.daw2.tallergo.crud_tallergo.enums;

/**
 * Enumeración que define las etapas del ciclo de vida de una reparación en el taller.
 */
public enum RepairStatus {
    /** * El vehículo está en el taller pero la intervención aún no ha comenzado.
     */
    STANDBY("En espera"),

    /** * El mecánico está trabajando actualmente en el vehículo.
     */
    ACTIVO("En reparación"),

    /** * La reparación ha concluido y el vehículo está listo para ser entregado al cliente.
     */
    FINALIZADO("Listo para recoger");

    private final String displayName;

    RepairStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}