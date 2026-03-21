package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentUpdateDTO;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {
    Page<AppointmentDTO> getAllAppointments(Pageable pageable);
    AppointmentDetailDTO getAppointmentById(Long id);
    Page<AppointmentDTO> getAppointmentsByUser(Long userId, Pageable pageable);
    Page<AppointmentDTO> getAppointmentsByWorkshop(Integer workshopId, Pageable pageable);
    AppointmentDTO createAppointment(AppointmentCreateDTO dto, String userEmail);
    AppointmentDTO updateAppointment(AppointmentUpdateDTO dto);
    void deleteAppointment(Long id);
    void updateStatus(Long id, AppointmentStatus newStatus);
}