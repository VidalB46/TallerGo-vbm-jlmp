package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import java.util.List;

/**
 * Mapper para la entidad Workshop.
 * Gestiona la conversión entre la persistencia y la presentación de los talleres,
 * incluyendo la relación con su plantilla de mecánicos en las vistas de detalle.
 */
public class WorkshopMapper {

    // ──────────────────────────────────────────────────────────────────────────
    // Entity -> DTO (Conversiones de Salida)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Convierte una entidad Workshop a un DTO básico para listados o búsquedas.
     * Utiliza el patrón Builder para una construcción limpia.
     */
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

    /**
     * Transforma una lista de entidades de taller en una lista de DTOs básicos.
     */
    public static List<WorkshopDTO> toDTOList(List<Workshop> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(WorkshopMapper::toDTO).toList();
    }

    /**
     * Convierte la entidad a un DTO de detalle completo.
     * Incluye la lista de mecánicos asociados transformados mediante MechanicMapper.
     */
    public static WorkshopDetailDTO toDetailDTO(Workshop entity) {
        if (entity == null) return null;
        WorkshopDetailDTO dto = new WorkshopDetailDTO();
        dto.setId(entity.getId());
        dto.setNif(entity.getNif());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());
        dto.setSchedule(entity.getSchedule());

        // Mapeo de la relación 1:N con mecánicos
        if (entity.getMechanics() != null) {
            dto.setMechanics(entity.getMechanics().stream()
                    .map(MechanicMapper::toDTO).toList());
        }
        return dto;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO -> Entity (Conversiones de Entrada)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Crea una nueva instancia de Workshop a partir de un DTO de creación.
     */
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

    /**
     * Actualiza una entidad de taller existente con los datos de un DTO de actualización.
     */
    public static void copyToExistingEntity(WorkshopUpdateDTO dto, Workshop entity) {
        if (dto == null || entity == null) return;
        entity.setNif(dto.getNif());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setLocation(dto.getLocation());
        entity.setEmail(dto.getEmail());
        entity.setSchedule(dto.getSchedule());
    }
}