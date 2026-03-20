package org.daw2.tallergo.crud_tallergo.enums;

/**
 * Enumeración que define las etapas del ciclo de vida de una reparación en el taller.
 */
public enum RepairStatus {
    /** * El vehículo está en el taller pero la intervención aún no ha comenzado.
     */
    STANDBY,

    /** * El mecánico está trabajando actualmente en el vehículo.
     */
    ACTIVO,

    /** * La reparación ha concluido y el vehículo está listo para ser entregado al cliente.
     */
    FINALIZADO
}