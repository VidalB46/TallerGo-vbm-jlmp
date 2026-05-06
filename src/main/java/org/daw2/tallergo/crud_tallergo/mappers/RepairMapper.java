package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.RepairCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;

/**
 * Clase utilitaria para mapear datos entre la entidad {@link Repair} y sus DTOs.
 * Todos los métodos son estáticos para evitar instanciación innecesaria.
 */
public class RepairMapper {

    /**
     * Convierte una entidad {@link Repair} a un DTO resumido para listados.
     *
     * @param entity Entidad de reparación (puede ser {@code null}).
     * @return DTO resumido con matrícula, modelo y estado, o {@code null}.
     */
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

    /**
     * Convierte una entidad {@link Repair} a un DTO de detalle completo.
     * Incluye vehículo, cita asociada y el presupuesto activo si existe.
     *
     * @param entity Entidad de reparación (puede ser {@code null}).
     * @return DTO de detalle, o {@code null} si la entidad es {@code null}.
     */
    public static RepairDetailDTO toDetailDTO(Repair entity) {
        if (entity == null) return null;

        RepairDetailDTO dto = new RepairDetailDTO();
        dto.setId(entity.getId());
        dto.setEntryDate(entity.getEntryDate());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());

        dto.setVehicle(VehicleMapper.toDTO(entity.getVehicle()));
        dto.setAppointment(AppointmentMapper.toDTO(entity.getAppointment()));

        // Mapeo del presupuesto para que la vista RepairDetail tenga acceso a budget.accepted
        if (entity.getBudget() != null) {
            dto.setBudget(BudgetMapper.toDTO(entity.getBudget()));
        }

        return dto;
    }

    /**
     * Crea una nueva entidad {@link Repair} a partir de un DTO de creación y sus relaciones.
     * El estado inicial de la reparación se establece en {@code STANDBY}.
     *
     * @param dto         DTO con los datos de creación (puede ser {@code null}).
     * @param appointment Cita asociada a la reparación.
     * @param vehicle     Vehículo al que pertenece la reparación.
     * @return Nueva entidad {@link Repair}, o {@code null} si el DTO es {@code null}.
     */
    public static Repair toEntity(RepairCreateDTO dto, Appointment appointment, Vehicle vehicle) {
        if (dto == null) return null;

        Repair entity = new Repair();
        entity.setEntryDate(dto.getEntryDate());
        entity.setNotes(dto.getNotes());
        entity.setStatus(RepairStatus.STANDBY);

        entity.setAppointment(appointment);
        entity.setVehicle(vehicle);

        return entity;
    }

    /**
     * Actualiza los campos de una entidad {@link Repair} existente con los valores del DTO.
     * Solo se aplican los campos no nulos del DTO.
     *
     * @param dto    DTO con los nuevos valores (puede ser {@code null}).
     * @param entity Entidad a actualizar (puede ser {@code null}).
     */
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