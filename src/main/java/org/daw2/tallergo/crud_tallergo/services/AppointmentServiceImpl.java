package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import org.daw2.tallergo.crud_tallergo.mappers.AppointmentMapper;
import org.daw2.tallergo.crud_tallergo.repositories.AppointmentRepository;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.repositories.VehicleRepository;
import org.daw2.tallergo.crud_tallergo.repositories.WorkshopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementación de la lógica de negocio para la gestión de citas.
 * Gestiona el ciclo de vida completo de una cita: creación, actualización de estado,
 * cambio de fecha y confirmación por parte del cliente.
 */
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final WorkshopRepository workshopRepository;

    /**
     * Devuelve una página paginada con todas las citas del sistema, con sus detalles cargados.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de cita.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAllWithDetails(pageable).map(AppointmentMapper::toDTO);
    }

    /**
     * Devuelve el detalle completo de una cita por su ID.
     *
     * @param id Identificador único de la cita.
     * @return DTO de detalle con usuario, vehículo y taller cargados.
     * @throws IllegalArgumentException si no existe ninguna cita con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public AppointmentDetailDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        return AppointmentMapper.toDetailDTO(appointment);
    }

    /**
     * Devuelve las citas de un usuario concreto con paginación.
     *
     * @param userId   Identificador del usuario propietario de las citas.
     * @param pageable Configuración de paginación.
     * @return Página de DTOs de cita del usuario indicado.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByUser(Long userId, Pageable pageable) {
        return appointmentRepository.findByUserIdWithDetails(userId, pageable)
                .map(AppointmentMapper::toDTO);
    }

    /**
     * Devuelve las citas de un taller concreto con paginación.
     *
     * @param workshopId Identificador del taller.
     * @param pageable   Configuración de paginación.
     * @return Página de DTOs de cita del taller indicado.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByWorkshop(Integer workshopId, Pageable pageable) {
        return appointmentRepository.findByWorkshopId(workshopId, pageable)
                .map(AppointmentMapper::toDTO);
    }

    /**
     * Crea una nueva cita validando que el vehículo pertenece al usuario autenticado.
     *
     * @param dto       DTO con los datos de la nueva cita (vehículo, taller, fecha).
     * @param userEmail Email del usuario autenticado que solicita la cita.
     * @return DTO de la cita recién creada.
     * @throws UsernameNotFoundException si no se encuentra el usuario por su email.
     * @throws IllegalArgumentException  si el vehículo o el taller no existen, o el vehículo
     *                                   no pertenece al usuario.
     */
    @Override
    @Transactional
    public AppointmentDTO createAppointment(AppointmentCreateDTO dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

        if (!vehicle.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("El vehículo no pertenece a este usuario");
        }

        Workshop workshop = workshopRepository.findById(dto.getWorkshopId())
                .orElseThrow(() -> new IllegalArgumentException("Taller no encontrado"));

        Appointment appointment = AppointmentMapper.toEntity(dto, user, workshop, vehicle);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return AppointmentMapper.toDTO(savedAppointment);
    }

    /**
     * Actualiza los datos editables de una cita existente.
     *
     * @param dto DTO con los nuevos valores, incluyendo el ID de la cita.
     * @return DTO actualizado de la cita.
     * @throws IllegalArgumentException si no existe la cita con el ID indicado.
     */
    @Override
    @Transactional
    public AppointmentDTO updateAppointment(AppointmentUpdateDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        AppointmentMapper.updateEntity(dto, appointment);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return AppointmentMapper.toDTO(updatedAppointment);
    }

    /**
     * Elimina una cita por su ID.
     *
     * @param id Identificador único de la cita a eliminar.
     * @throws IllegalArgumentException si no existe ninguna cita con ese ID.
     */
    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Cita no encontrada");
        }
        appointmentRepository.deleteById(id);
    }

    /**
     * Cambia el estado de una cita al nuevo estado indicado.
     *
     * @param id        Identificador único de la cita.
     * @param newStatus Nuevo estado a asignar a la cita.
     * @throws IllegalArgumentException si no existe ninguna cita con ese ID.
     */
    @Override
    @Transactional
    public void updateStatus(Long id, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }

    /**
     * Actualiza la fecha de una cita propuesta por el taller.
     * Resetea la aceptación del cliente al cambiar la fecha.
     *
     * @param id      Identificador único de la cita.
     * @param newDate Nueva fecha y hora propuesta para la cita.
     * @throws IllegalArgumentException si no existe ninguna cita con ese ID.
     */
    @Override
    @Transactional
    public void updateDate(Long id, LocalDateTime newDate) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        appointment.setStartDate(newDate);
        appointment.setIsDateAcceptedByClient(false);
        appointmentRepository.save(appointment);
    }

    /**
     * Registra la aceptación de la fecha por parte del cliente y confirma la cita automáticamente.
     *
     * @param id Identificador único de la cita.
     * @throws IllegalArgumentException si no existe ninguna cita con ese ID.
     */
    @Override
    @Transactional
    public void acceptDate(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        // El cliente acepta la fecha
        appointment.setIsDateAcceptedByClient(true);
        // Como el taller fue quien propuso la fecha, la damos por confirmada automáticamente
        appointment.setStatus(AppointmentStatus.CONFIRMADO);

        appointmentRepository.save(appointment);
    }
}