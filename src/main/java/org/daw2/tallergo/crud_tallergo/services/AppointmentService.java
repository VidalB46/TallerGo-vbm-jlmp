package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentUpdateDTO;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * Interfaz de servicio para la gestión del ciclo de vida de las citas.
 * Define las operaciones disponibles para clientes, mecánicos y administradores.
 */
public interface AppointmentService {

    /**
     * Devuelve una página paginada con todas las citas del sistema.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de cita.
     */
    Page<AppointmentDTO> getAllAppointments(Pageable pageable);

    /**
     * Devuelve el detalle completo de una cita por su ID.
     *
     * @param id Identificador único de la cita.
     * @return DTO de detalle con usuario, vehículo y taller cargados.
     */
    AppointmentDetailDTO getAppointmentById(Long id);

    /**
     * Devuelve las citas de un usuario concreto con paginación.
     *
     * @param userId   Identificador del usuario propietario.
     * @param pageable Configuración de paginación.
     * @return Página de DTOs de cita del usuario.
     */
    Page<AppointmentDTO> getAppointmentsByUser(Long userId, Pageable pageable);

    /**
     * Devuelve SOLO las citas activas (no archivadas) de un usuario.
     */
    Page<AppointmentDTO> getActiveAppointmentsByUser(Long userId, Pageable pageable);

    /**
     * Devuelve las citas de un taller concreto con paginación.
     *
     * @param workshopId Identificador del taller.
     * @param pageable   Configuración de paginación.
     * @return Página de DTOs de cita del taller.
     */
    Page<AppointmentDTO> getAppointmentsByWorkshop(Integer workshopId, Pageable pageable);

    /**
     * Crea una nueva cita validando que el vehículo pertenece al usuario autenticado.
     *
     * @param dto       DTO con los datos de la nueva cita.
     * @param userEmail Email del usuario autenticado que solicita la cita.
     * @return DTO de la cita creada.
     */
    AppointmentDTO createAppointment(AppointmentCreateDTO dto, String userEmail);

    /**
     * Actualiza los datos editables de una cita existente.
     *
     * @param dto DTO con los nuevos valores e ID de la cita.
     * @return DTO actualizado de la cita.
     */
    AppointmentDTO updateAppointment(AppointmentUpdateDTO dto);

    /**
     * Elimina una cita por su ID.
     *
     * @param id Identificador único de la cita a eliminar.
     */
    void deleteAppointment(Long id);

    /**
     * Cambia el estado de una cita al nuevo estado indicado.
     *
     * @param id        Identificador de la cita.
     * @param newStatus Nuevo estado a asignar.
     */
    void updateStatus(Long id, AppointmentStatus newStatus);

    /**
     * Actualiza la fecha propuesta para una cita y resetea la aceptación del cliente.
     *
     * @param id      Identificador de la cita.
     * @param newDate Nueva fecha y hora propuesta.
     */
    void updateDate(Long id, LocalDateTime newDate);

    /**
     * Registra la aceptación de la fecha por el cliente y confirma la cita automáticamente.
     *
     * @param id Identificador de la cita.
     */
    void acceptDate(Long id);

    void archiveAppointment(Long id);
}