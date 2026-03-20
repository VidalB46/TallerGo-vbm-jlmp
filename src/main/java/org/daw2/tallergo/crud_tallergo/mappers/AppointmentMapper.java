package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;

public class AppointmentMapper {

    /**
     * Convierte una Entidad en un DTO ligero para listados.
     */
    public static AppointmentDTO toDTO(Appointment entity) {
        if (entity == null) return null;

        return AppointmentDTO.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .status(entity.getStatus())
                // Extraemos los nombres directamente para no enviar objetos pesados
                .vehicleModel(entity.getVehicle() != null ? entity.getVehicle().getMatricula() + " - " + entity.getVehicle().getModel() : "Desconocido")
                .workshopName(entity.getWorkshop() != null ? entity.getWorkshop().getName() : "Desconocido")
                .userEmail(entity.getUser() != null ? entity.getUser().getEmail() : "Desconocido")
                .build();
    }

    /**
     * Convierte una Entidad en un DTO Detallado (para la vista "Ver más").
     */
    public static AppointmentDetailDTO toDetailDTO(Appointment entity) {
        if (entity == null) return null;

        AppointmentDetailDTO dto = new AppointmentDetailDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        dto.setMediaUrl(entity.getMediaUrl());

        dto.setUserEmail(entity.getUser() != null ? entity.getUser().getEmail() : "Desconocido");
        dto.setVehicle(VehicleMapper.toDTO(entity.getVehicle()));
        dto.setWorkshop(WorkshopMapper.toDTO(entity.getWorkshop()));

        return dto;
    }

    /**
     * Convierte un DTO de creación en una nueva Entidad Appointment.
     * Requiere que le pasemos las entidades reales ya buscadas en la base de datos.
     */
    public static Appointment toEntity(AppointmentCreateDTO dto, User user, Workshop workshop, Vehicle vehicle) {
        if (dto == null) return null;

        Appointment entity = new Appointment();
        entity.setStartDate(dto.getStartDate());
        entity.setNotes(dto.getNotes());
        // Por defecto, una cita nueva siempre nace como SOLICITADA
        entity.setStatus(AppointmentStatus.SOLICITADO);

        entity.setUser(user);
        entity.setWorkshop(workshop);
        entity.setVehicle(vehicle);

        return entity;
    }

    /**
     * Actualiza una Entidad existente con los datos de un UpdateDTO.
     */
    public static void updateEntity(AppointmentUpdateDTO dto, Appointment entity) {
        if (dto == null || entity == null) return;

        if (dto.getStartDate() != null) {
            entity.setStartDate(dto.getStartDate());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
    }
}