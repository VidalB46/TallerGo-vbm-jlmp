package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import java.util.ArrayList;
import java.util.List;

public class WorkshopMapper {

    public static WorkshopDTO toDTO(Workshop entity) {
        if (entity == null) return null;
        return WorkshopDTO.builder()
                .id(entity.getId())
                .nif(entity.getNif())
                .name(entity.getName())
                .location(entity.getLocation())
                .phone(entity.getPhone())
                .build();
    }

    public static List<WorkshopDTO> toDTOList(List<Workshop> entities) {
        return entities.stream().map(WorkshopMapper::toDTO).toList();
    }

    public static WorkshopDetailDTO toDetailDTO(Workshop entity) {
        if (entity == null) return null;
        WorkshopDetailDTO dto = new WorkshopDetailDTO();
        dto.setId(entity.getId());
        dto.setNif(entity.getNif());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());
        dto.setSchedule(entity.getSchedule());

        if (entity.getMechanics() != null) {
            dto.setMechanics(entity.getMechanics().stream()
                    .map(MechanicMapper::toDTO).toList());
        }
        return dto;
    }

    public static Workshop toEntity(WorkshopCreateDTO dto) {
        if (dto == null) return null;
        Workshop entity = new Workshop();
        entity.setNif(dto.getNif());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setLocation(dto.getLocation());
        entity.setEmail(dto.getEmail());
        entity.setSchedule(dto.getSchedule());
        return entity;
    }

    public static void copyToExistingEntity(WorkshopUpdateDTO dto, Workshop entity) {
        entity.setNif(dto.getNif());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setLocation(dto.getLocation());
        entity.setEmail(dto.getEmail());
        entity.setSchedule(dto.getSchedule());
    }
}