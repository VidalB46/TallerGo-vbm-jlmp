package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Mecanico;
import org.daw2.tallergo.crud_tallergo.entities.Taller;

import java.util.List;
import java.util.stream.Collectors;

public class MecanicoMapper {

    // ───────────────────────────────
    // ENTITY → DTO
    // ───────────────────────────────
    public static MecanicoDTO toDTO(Mecanico entity) {
        if (entity == null) return null;

        MecanicoDTO dto = new MecanicoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEspecialidad(entity.getEspecialidad());

        if (entity.getTaller() != null) {
            dto.setTallerId(entity.getTaller().getId());
            dto.setTallerNombre(entity.getTaller().getNombre());
        }

        return dto;
    }

    public static List<MecanicoDTO> toDTOList(List<Mecanico> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(MecanicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ───────────────────────────────
    // ENTITY → DETAIL DTO
    // ───────────────────────────────
    public static MecanicoDetailDTO toDetailDTO(Mecanico entity) {
        if (entity == null) return null;

        MecanicoDetailDTO dto = new MecanicoDetailDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEspecialidad(entity.getEspecialidad());

        if (entity.getTaller() != null) {
            dto.setTallerId(entity.getTaller().getId());
            dto.setTallerNombre(entity.getTaller().getNombre());
            dto.setTallerDireccion(entity.getTaller().getDireccion());
        }

        return dto;
    }

    // ───────────────────────────────
    // ENTITY → UPDATE DTO
    // ───────────────────────────────
    public static MecanicoUpdateDTO toUpdateDTO(Mecanico entity) {
        if (entity == null) return null;

        MecanicoUpdateDTO dto = new MecanicoUpdateDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEspecialidad(entity.getEspecialidad());

        if (entity.getTaller() != null) {
            dto.setTallerId(entity.getTaller().getId());
        }

        return dto;
    }


    // ───────────────────────────────
    // DTO → ENTITY
    // ───────────────────────────────
    public static Mecanico toEntity(MecanicoDTO dto, Taller taller) {
        if (dto == null) return null;

        Mecanico entity = new Mecanico();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setEspecialidad(dto.getEspecialidad());
        entity.setTaller(taller);

        return entity;
    }

    public static Mecanico toEntity(MecanicoCreateDTO dto) {
        if (dto == null) return null;

        Mecanico entity = new Mecanico();
        entity.setNombre(dto.getNombre());
        entity.setEspecialidad(dto.getEspecialidad());

        if (dto.getTallerId() != null) {
            Taller taller = new Taller();
            taller.setId(dto.getTallerId());
            entity.setTaller(taller);
        }

        return entity;
    }

    // ───────────────────────────────
    // COPIAR A ENTITY EXISTENTE
    // ───────────────────────────────
    public static void copyToExistingEntity(MecanicoDTO dto, Mecanico entity, Taller taller) {
        if (dto == null || entity == null) return;

        entity.setNombre(dto.getNombre());
        entity.setEspecialidad(dto.getEspecialidad());
        entity.setTaller(taller);
    }

    public static void copyToExistingEntity(MecanicoCreateDTO dto, Mecanico entity) {
        if (dto == null || entity == null) return;

        entity.setNombre(dto.getNombre());
        entity.setEspecialidad(dto.getEspecialidad());

        if (dto.getTallerId() != null) {
            Taller taller = new Taller();
            taller.setId(dto.getTallerId());
            entity.setTaller(taller);
        }
    }

    public static void copyToExistingEntity(MecanicoUpdateDTO dto, Mecanico entity) {
        if (dto == null || entity == null) return;

        entity.setNombre(dto.getNombre());
        entity.setEspecialidad(dto.getEspecialidad());

        if (dto.getTallerId() != null) {
            Taller taller = new Taller();
            taller.setId(dto.getTallerId());
            entity.setTaller(taller);
        }
    }


}