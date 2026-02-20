package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.TallerDTO;
import org.daw2.tallergo.crud_tallergo.entities.Taller;

import java.util.List;
import java.util.stream.Collectors;

public class TallerMapper {

    // ───────────────────────────────
    // ENTITY → DTO
    // ───────────────────────────────
    public static TallerDTO toDTO(Taller entity) {
        if (entity == null) return null;

        TallerDTO dto = new TallerDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDireccion(entity.getDireccion());

        return dto;
    }

    public static List<TallerDTO> toDTOList(List<Taller> entities) {
        if (entities == null) return List.of();

        return entities.stream()
                .map(TallerMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ───────────────────────────────
    // DTO → ENTITY
    // ───────────────────────────────
    public static Taller toEntity(TallerDTO dto) {
        if (dto == null) return null;

        Taller entity = new Taller();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());

        return entity;
    }

    // ───────────────────────────────
    // COPIAR A ENTITY EXISTENTE
    // ───────────────────────────────
    public static void copyToExistingEntity(TallerDTO dto, Taller entity) {
        if (dto == null || entity == null) return;

        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
    }
}