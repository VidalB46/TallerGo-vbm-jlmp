package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairUpdateDTO;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz de servicio que define las operaciones de negocio para la gestión de expedientes de reparación.
 */
public interface RepairService {

    /**
     * Recupera la información detallada de una reparación específica.
     * @param id Identificador único de la reparación.
     * @return DTO con los detalles técnicos y asociaciones.
     */
    RepairDetailDTO getRepairById(Long id);

    /**
     * Obtiene un listado paginado de todas las reparaciones del sistema.
     * @param pageable Configuración de paginación y ordenación.
     * @return Página de objetos RepairDTO.
     */
    Page<RepairDTO> getAllRepairs(Pageable pageable);

    /**
     * Actualiza los datos editables de una orden de reparación.
     * @param dto Contenedor con los datos actualizados.
     * @return DTO de la reparación tras persistir cambios.
     */
    RepairDTO updateRepair(RepairUpdateDTO dto);

    /**
     * Modifica el estado del proceso de reparación y sincroniza la cita vinculada.
     * @param id Identificador de la reparación.
     * @param newStatus Nuevo estado a asignar (ACTIVO, FINALIZADO, etc.).
     */
    void updateStatus(Long id, RepairStatus newStatus);

    /**
     * Registra la entrega final del vehículo al propietario.
     * @param id Identificador de la reparación finalizada.
     */
    void deliverVehicle(Long id);

    /**
     * Genera automáticamente un expediente de reparación al confirmar una cita.
     * Facilita la peritación previa sin presencia física del vehículo.
     * @param appointmentId Identificador de la cita confirmada.
     */
    void createAutomaticRepair(Long appointmentId);

    /**
     * Formaliza la entrada física del vehículo en el taller.
     * @param repairId Identificador del expediente de reparación existente.
     */
    void receiveVehicle(Long repairId);
}