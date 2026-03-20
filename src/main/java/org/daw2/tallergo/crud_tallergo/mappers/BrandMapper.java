package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;

import java.util.List;
import java.util.Set;

/**
 * Mapper manual para la entidad Brand.
 * Gestiona la conversión entre la entidad de base de datos y sus diferentes representaciones DTO.
 */
public class BrandMapper {

    // -------------------------------------------------------------------
    // Entity -> DTO (Conversiones de salida)
    // -------------------------------------------------------------------

    /**
     * Convierte una entidad Brand a un DTO básico (sin colecciones anidadas).
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
     * Convierte una lista de entidades a una lista de DTOs básicos.
     */
    public static List<BrandDTO> toDTOList(List<Brand> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(BrandMapper::toDTO).toList();
    }

    /**
     * Convierte una entidad Brand a un DTO de detalle, incluyendo su lista de vehículos.
     */
    public static BrandDetailDTO toDetailDTO(Brand entity) {
        if (entity == null) return null;

        BrandDetailDTO dto = new BrandDetailDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());

        // Mapeo de la relación 1:N (Vehículos de la marca)
        dto.setVehicles(toVehicleList(entity.getVehicles()));
        return dto;
    }

    /**
     * Mapeo auxiliar de Vehicle a VehicleDTO para evitar dependencias circulares pesadas.
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
        return dto;
    }

    /**
     * Transforma el Set de vehículos de la entidad en una List para el DTO.
     */
    public static List<VehicleDTO> toVehicleList(Set<Vehicle> vehicles) {
        if (vehicles == null) return List.of();
        return vehicles.stream().map(BrandMapper::toVehicleDTO).toList();
    }

    /**
     * Convierte la entidad a un DTO preparado para formularios de actualización.
     */
    public static BrandUpdateDTO toUpdateDTO(Brand entity) {
        if (entity == null) return null;
        BrandUpdateDTO dto = new BrandUpdateDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCountry(entity.getCountry());
        return dto;
    }

    // -------------------------------------------------------------------
    // DTO -> Entity (Conversiones de entrada)
    // -------------------------------------------------------------------

    /**
     * Crea una nueva instancia de Brand a partir de un DTO de creación.
     */
    public static Brand toEntity(BrandCreateDTO dto) {
        if (dto == null) return null;
        Brand e = new Brand();
        e.setName(dto.getName());
        e.setCountry(dto.getCountry());
        return e;
    }

    /**
     * Convierte un DTO de actualización en una entidad (incluyendo el ID).
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
     * Copia los valores de un DTO de actualización sobre una entidad ya existente.
     * Útil para persistencia gestionada por JPA (mantiene la instancia original).
     */
    public static void copyToExistingEntity(BrandUpdateDTO dto, Brand entity) {
        if (dto == null || entity == null) return;
        entity.setName(dto.getName());
        entity.setCountry(dto.getCountry());
    }
}