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

/**
 * Clase utilitaria para mapear datos entre la entidad {@link Appointment} y sus DTOs.
 * Todos los métodos son estáticos para evitar instanciación innecesaria.
 */
public class AppointmentMapper {

    /**
     * Convierte una entidad {@link Appointment} a un DTO resumido para listados.
     *
     * @param entity Entidad de cita (puede ser {@code null}).
     * @return DTO resumido, o {@code null} si la entidad es {@code null}.
     */
    public static AppointmentDTO toDTO(Appointment entity) {
        if (entity == null) return null;

        AppointmentDTO dto = AppointmentDTO.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .status(entity.getStatus())
                .vehicleModel(entity.getVehicle() != null ? entity.getVehicle().getMatricula() + " - " + entity.getVehicle().getModel() : "Desconocido")
                .workshopName(entity.getWorkshop() != null ? entity.getWorkshop().getName() : "Desconocido")
                .userEmail(entity.getUser() != null ? entity.getUser().getEmail() : "Desconocido")
                .build();


        if (entity.getRepair() != null && entity.getRepair().getBudget() != null) {
            dto.setHasBudget(true);
            dto.setIsBudgetAccepted(Boolean.TRUE.equals(entity.getRepair().getBudget().getAccepted()));
        } else {
            dto.setHasBudget(false);
            dto.setIsBudgetAccepted(false);
        }

        return dto;
    }

    /**
     * Convierte una entidad {@link Appointment} a un DTO de detalle completo.
     *
     * @param entity Entidad de cita (puede ser {@code null}).
     * @return DTO de detalle con vehículo, taller y estado del presupuesto, o {@code null}.
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

        dto.setIsDateAcceptedByClient(entity.getIsDateAcceptedByClient() != null ? entity.getIsDateAcceptedByClient() : true);

        dto.setUserEmail(entity.getUser() != null ? entity.getUser().getEmail() : "Desconocido");
        dto.setVehicle(VehicleMapper.toDTO(entity.getVehicle()));
        dto.setWorkshop(WorkshopMapper.toDTO(entity.getWorkshop()));


        if (entity.getRepair() != null) {
            dto.setRepairId(entity.getRepair().getId());
            dto.setHasBudget(entity.getRepair().getBudget() != null);
            dto.setIsBudgetAccepted(
                    entity.getRepair().getBudget() != null &&
                            Boolean.TRUE.equals(entity.getRepair().getBudget().getAccepted())
            );
        } else {
            dto.setHasBudget(false);
            dto.setIsBudgetAccepted(false);
        }

        return dto;
    }

    /**
     * Crea una nueva entidad {@link Appointment} a partir de un DTO de creación y sus relaciones.
     *
     * @param dto      DTO con los datos de la nueva cita (puede ser {@code null}).
     * @param user     Usuario propietario de la cita.
     * @param workshop Taller donde se realizará la cita.
     * @param vehicle  Vehículo implicado en la cita.
     * @return Nueva entidad de cita, o {@code null} si el DTO es {@code null}.
     */
    public static Appointment toEntity(AppointmentCreateDTO dto, User user, Workshop workshop, Vehicle vehicle) {
        if (dto == null) return null;

        Appointment entity = new Appointment();
        entity.setStartDate(dto.getStartDate());
        entity.setNotes(dto.getNotes());
        entity.setMediaUrl(dto.getMediaUrl());
        entity.setStatus(AppointmentStatus.SOLICITADO);

        // Al crear, el cliente asume su propia fecha por defecto
        entity.setIsDateAcceptedByClient(true);

        entity.setUser(user);
        entity.setWorkshop(workshop);
        entity.setVehicle(vehicle);

        return entity;
    }

    /**
     * Actualiza los campos modificables de una entidad {@link Appointment} existente.
     *
     * @param dto    DTO con los nuevos valores (campos nulos se ignoran).
     * @param entity Entidad de cita a actualizar en base de datos.
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