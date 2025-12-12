package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;

import java.util.List;

/**
 * Mapper utilitario entre la entidad Brand y sus DTOs.
 * Implementación simple sin frameworks de mapeo.
 */
public class BrandMapper {

    // ------------------------------------------------
    // Entity -> DTO (listado/tabla básico)
    // ------------------------------------------------

    /**
     * Convierte una entidad {@link Brand} a {@link BrandDTO}.
     */
    public static BrandDTO toDTO(Brand entity) {
        if (entity == null) return null;
        BrandDTO dto = new BrandDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());
        return dto;
    }

    /**
     * Convierte una lista de entidades {@link Brand} a {@link BrandDTO}.
     */
    public static List<BrandDTO> toDTOList(List<Brand> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(BrandMapper::toDTO).toList();
    }

    // ------------------------------------------------
    // Entity -> DTO (detalle con vehículos)
    // ------------------------------------------------

    /**
     * Convierte una {@link Brand} a {@link BrandDetailDTO}, mapeando su lista de vehículos.
     */
    public static BrandDetailDTO toDetailDTO(Brand entity) {
        if (entity == null) return null;

        BrandDetailDTO dto = new BrandDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());
        // Mapeamos los vehículos usando el método auxiliar
        dto.setVehicles(toVehicleList(entity.getVehicles()));
        return dto;
    }

    /**
     * Auxiliar: Convierte una entidad {@link Vehicle} a {@link VehicleDTO}.
     * Usado para rellenar la lista dentro del detalle de la marca.
     */
    public static VehicleDTO toVehicleDTO(Vehicle v) {
        if (v == null) return null;
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setModel(v.getModel());
        dto.setMatricula(v.getMatricula());
        dto.setColor(v.getColor());
        dto.setYear(v.getYear());
        dto.setKm(v.getKm());
        // En el listado dentro de la marca, ya sabemos la marca, no hace falta poner brandName
        return dto;
    }

    /**
     * Auxiliar: Convierte una lista de {@link Vehicle} a {@link VehicleDTO}.
     */
    public static List<VehicleDTO> toVehicleList(List<Vehicle> vehicles) {
        if (vehicles == null) return List.of();
        return vehicles.stream().map(BrandMapper::toVehicleDTO).toList();
    }

    /**
     * Convierte una entidad {@link Brand} a {@link BrandUpdateDTO} para precargar el formulario.
     */
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

    /**
     * Crea una nueva entidad {@link Brand} desde un {@link BrandCreateDTO}.
     */
    public static Brand toEntity(BrandCreateDTO dto) {
        if (dto == null) return null;
        Brand e = new Brand();
        e.setName(dto.getName());
        e.setCountry(dto.getCountry());
        return e;
    }

    /**
     * Crea una entidad {@link Brand} desde un {@link BrandUpdateDTO}.
     */
    public static Brand toEntity(BrandUpdateDTO dto) {
        if (dto == null) return null;
        Brand e = new Brand();
        e.setId(dto.getId());
        e.setName(dto.getName());
        e.setCountry(dto.getCountry());
        return e;
    }

    /**
     * Copia campos editables de {@link BrandUpdateDTO} sobre una entidad existente.
     */
    public static void copyToExistingEntity(BrandUpdateDTO dto, Brand entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
    }
}
