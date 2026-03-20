package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz que define la lógica de negocio para la gestión de citas.
 */
public interface AppointmentService {

    /**
     * Obtiene todas las citas del sistema paginadas (Para la vista de Administrador).
     */
    Page<AppointmentDTO> getAllAppointments(Pageable pageable);

    /**
     * Obtiene una cita por su ID con todo el detalle cargado.
     */
    AppointmentDetailDTO getAppointmentById(Long id);

    /**
     * Obtiene las citas paginadas de un usuario concreto (Para la vista "Mis Citas").
     */
    Page<AppointmentDTO> getAppointmentsByUser(Long userId, Pageable pageable);

    /**
     * Obtiene las citas paginadas de un taller concreto (Para la agenda del mecánico/admin).
     */
    Page<AppointmentDTO> getAppointmentsByWorkshop(Integer workshopId, Pageable pageable);

    /**
     * Crea una nueva cita validando que el vehículo, el taller y el usuario existen.
     */
    AppointmentDTO createAppointment(AppointmentCreateDTO dto, String userEmail);

    /**
     * Actualiza el estado o la fecha de una cita existente.
     */
    AppointmentDTO updateAppointment(AppointmentUpdateDTO dto);

    /**
     * Cancela o elimina una cita.
     */
    void deleteAppointment(Long id);
}