package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDetailDTO; // Añadido
import org.daw2.tallergo.crud_tallergo.dtos.MechanicUpdateDTO; // Añadido
import org.daw2.tallergo.crud_tallergo.entities.Mechanic;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;

import java.util.List;

/**
 * Mapper para la entidad Mechanic.
 * Transforma entre la entidad JPA y los diferentes DTOs de la capa de presentación.
 */
public class MechanicMapper {

    // ────────────────────────────────────────────────
    // Entity -> DTO (Listado básico)
    // ────────────────────────────────────────────────
    public static MechanicDTO toDTO(Mechanic entity) {
        if (entity == null) return null;
        MechanicDTO dto = new MechanicDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecialty(entity.getSpecialty());
        if (entity.getWorkshop() != null) {
            dto.setWorkshopName(entity.getWorkshop().getName());
        }
        return dto;
    }

    public static List<MechanicDTO> toDTOList(List<Mechanic> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(MechanicMapper::toDTO).toList();
    }

    // ────────────────────────────────────────────────
    // Entity -> DetailDTO (Vista de detalle)
    // ────────────────────────────────────────────────
    public static MechanicDetailDTO toDetailDTO(Mechanic entity) {
        if (entity == null) return null;
        MechanicDetailDTO dto = new MechanicDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecialty(entity.getSpecialty());

        // Usamos el WorkshopMapper para obtener el DTO del taller asociado
        if (entity.getWorkshop() != null) {
            dto.setWorkshop(WorkshopMapper.toDTO(entity.getWorkshop()));
        }
        return dto;
    }

    // ────────────────────────────────────────────────
    // Entity -> UpdateDTO (Cargar formulario de edición)
    // ────────────────────────────────────────────────
    public static MechanicUpdateDTO toUpdateDTO(Mechanic entity) {
        if (entity == null) return null;
        return MechanicUpdateDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .specialty(entity.getSpecialty())
                .workshopId(entity.getWorkshop() != null ? entity.getWorkshop().getId() : null)
                .build();
    }

    // ────────────────────────────────────────────────
    // DTO -> Entity (Creación y Actualización)
    // ────────────────────────────────────────────────
    public static Mechanic toEntity(MechanicCreateDTO dto) {
        if (dto == null) return null;
        Mechanic entity = new Mechanic();
        entity.setName(dto.getName());
        entity.setSpecialty(dto.getSpecialty());

        // Creamos una entidad Workshop mínima (solo ID) para la relación
        Workshop w = new Workshop();
        w.setId(dto.getWorkshopId());
        entity.setWorkshop(w);

        return entity;
    }

    public static void copyToExistingEntity(MechanicUpdateDTO dto, Mechanic entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setSpecialty(dto.getSpecialty());

        if (dto.getWorkshopId() != null) {
            Workshop w = new Workshop();
            w.setId(dto.getWorkshopId());
            entity.setWorkshop(w);
        }
    }
}