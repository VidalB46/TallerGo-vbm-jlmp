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