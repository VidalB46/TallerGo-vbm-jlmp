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

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final WorkshopRepository workshopRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDetailDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        return AppointmentMapper.toDetailDTO(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByUser(Long userId, Pageable pageable) {
        return appointmentRepository.findByUserId(userId, pageable)
                .map(AppointmentMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAppointmentsByWorkshop(Integer workshopId, Pageable pageable) {
        return appointmentRepository.findByWorkshopId(workshopId, pageable)
                .map(AppointmentMapper::toDTO);
    }

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

    @Override
    @Transactional
    public AppointmentDTO updateAppointment(AppointmentUpdateDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        AppointmentMapper.updateEntity(dto, appointment);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return AppointmentMapper.toDTO(updatedAppointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Cita no encontrada");
        }
        appointmentRepository.deleteById(id);
    }
}