package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;

import java.util.List;
import java.util.Set;

public class BrandMapper {

    // ------------------------------------------------
    // Entity -> DTO (listado/tabla básico)
    // ------------------------------------------------

    public static BrandDTO toDTO(Brand entity) {
        if (entity == null) return null;
        BrandDTO dto = new BrandDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());
        return dto;
    }

    public static List<BrandDTO> toDTOList(List<Brand> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(BrandMapper::toDTO).toList();
    }

    // ------------------------------------------------
    // Entity -> DTO (detalle con vehículos)
    // ------------------------------------------------

    public static BrandDetailDTO toDetailDTO(Brand entity) {
        if (entity == null) return null;

        BrandDetailDTO dto = new BrandDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());

        dto.setVehicles(toVehicleList(entity.getVehicles()));
        return dto;
    }

    public static VehicleDTO toVehicleDTO(Vehicle v) {
        if (v == null) return null;
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setModel(v.getModel());
        dto.setMatricula(v.getMatricula());
        dto.setColor(v.getColor());
        dto.setYear(v.getYear());
        dto.setKm(v.getKm());

        return dto;
    }

    // --- CORRECCIÓN AQUÍ: Cambiado List<Vehicle> por Set<Vehicle> ---
    public static List<VehicleDTO> toVehicleList(Set<Vehicle> vehicles) {
        if (vehicles == null) return List.of();
        // Convertimos el Set a Stream y luego lo recogemos en una List para el DTO
        return vehicles.stream().map(BrandMapper::toVehicleDTO).toList();
    }

    public static BrandUpdateDTO toUpdateDTO(Brand entity) {
        if (entity == null) return null;
        BrandUpdateDTO dto = new BrandUpdateDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());
        return dto;
    }

    // ------------------------------------------------
    // DTO (create/update) -> Entity
    // ------------------------------------------------

    public static Brand toEntity(BrandCreateDTO dto) {
        if (dto == null) return null;
        Brand e = new Brand();
        e.setName(dto.getName());
        e.setCountry(dto.getCountry());
        return e;
    }

    public static Brand toEntity(BrandUpdateDTO dto) {
        if (dto == null) return null;
        Brand e = new Brand();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setCountry(dto.getCountry());
        return e;
    }

    public static void copyToExistingEntity(BrandUpdateDTO dto, Brand entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
    }
}