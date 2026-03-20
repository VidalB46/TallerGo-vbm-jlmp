package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Mechanic;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;

import java.util.List;

/**
 * Mapper para la entidad Mechanic.
 * Gestiona la transformación entre el modelo de persistencia y los objetos de transferencia de datos (DTO).
 */
public class MechanicMapper {

    // ──────────────────────────────────────────────────────────────────────────
    // Entity -> DTO (Conversiones de Salida)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Convierte una entidad Mechanic a un DTO simplificado para listados.
     */
    public static MechanicDTO toDTO(Mechanic entity) {
        if (entity == null) return null;
        MechanicDTO dto = new MechanicDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecialty(entity.getSpecialty());

        // Mapeo aplanado (Flattening) del nombre del taller
        if (entity.getWorkshop() != null) {
            dto.setWorkshopName(entity.getWorkshop().getName());
        }
        return dto;
    }

    /**
     * Convierte una colección de entidades en una lista de DTOs básicos.
     */
    public static List<MechanicDTO> toDTOList(List<Mechanic> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(MechanicMapper::toDTO).toList();
    }

    /**
     * Convierte una entidad a un DTO de detalle completo, incluyendo el objeto WorkshopDTO.
     */
    public static MechanicDetailDTO toDetailDTO(Mechanic entity) {
        if (entity == null) return null;
        MechanicDetailDTO dto = new MechanicDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecialty(entity.getSpecialty());

        // Reutilización del WorkshopMapper para mantener la jerarquía de objetos
        if (entity.getWorkshop() != null) {
            dto.setWorkshop(WorkshopMapper.toDTO(entity.getWorkshop()));
        }
        return dto;
    }

    /**
     * Prepara un DTO de actualización a partir de una entidad existente.
     * Utiliza el patrón Builder si está disponible en el DTO.
     */
    public static MechanicUpdateDTO toUpdateDTO(Mechanic entity) {
        if (entity == null) return null;
        return MechanicUpdateDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .specialty(entity.getSpecialty())
                .workshopId(entity.getWorkshop() != null ? entity.getWorkshop().getId() : null)
                .build();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO -> Entity (Conversiones de Entrada)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Transforma un DTO de creación en una nueva entidad Mechanic.
     * Nota: El taller se asocia mediante una instancia "proxy" (solo ID).
     */
    public static Mechanic toEntity(MechanicCreateDTO dto) {
        if (dto == null) return null;
        Mechanic entity = new Mechanic();
        entity.setName(dto.getName());
        entity.setSpecialty(dto.getSpecialty());

        if (dto.getWorkshopId() != null) {
            Workshop w = new Workshop();
            w.setId(dto.getWorkshopId());
            entity.setWorkshop(w);
        }

        return entity;
    }

    /**
     * Actualiza el estado de una entidad persistente con los datos de un DTO de actualización.
     */
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