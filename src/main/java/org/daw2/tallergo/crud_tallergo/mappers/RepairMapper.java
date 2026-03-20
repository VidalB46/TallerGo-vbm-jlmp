package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.RepairCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;

public class RepairMapper {

    public static RepairDTO toDTO(Repair entity) {
        if (entity == null) return null;

        RepairDTO dto = new RepairDTO();
        dto.setId(entity.getId());
        dto.setEntryDate(entity.getEntryDate());
        dto.setStatus(entity.getStatus());

        if (entity.getVehicle() != null) {
            dto.setVehicleMatricula(entity.getVehicle().getMatricula());
            dto.setVehicleModel(entity.getVehicle().getModel());
        }
        if (entity.getAppointment() != null) {
            dto.setAppointmentId(entity.getAppointment().getId());
        }

        return dto;
    }

    public static RepairDetailDTO toDetailDTO(Repair entity) {
        if (entity == null) return null;

        RepairDetailDTO dto = new RepairDetailDTO();
        dto.setId(entity.getId());
        dto.setEntryDate(entity.getEntryDate());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        dto.setVehicle(VehicleMapper.toDTO(entity.getVehicle()));
        dto.setAppointment(AppointmentMapper.toDTO(entity.getAppointment()));
        dto.setBudget(BudgetMapper.toDTO(entity.getBudget()));

        return dto;
    }

    public static Repair toEntity(RepairCreateDTO dto, Appointment appointment, Vehicle vehicle) {
        if (dto == null) return null;

        Repair entity = new Repair();
        entity.setEntryDate(dto.getEntryDate());
        entity.setNotes(dto.getNotes());
        entity.setStatus(RepairStatus.STANDBY); // Estado inicial por defecto

        entity.setAppointment(appointment);
        entity.setVehicle(vehicle);

        return entity;
    }

    public static void updateEntity(RepairUpdateDTO dto, Repair entity) {
        if (dto == null || entity == null) return;

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
    }
}