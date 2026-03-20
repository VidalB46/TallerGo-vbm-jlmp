package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;

import java.util.List;

/**
 * Mapper para la entidad Vehicle.
 * Se encarga de transformar los datos entre el modelo de persistencia y los DTOs,
 * gestionando la relación con la marca (Brand) ya sea de forma plana o detallada.
 */
public class VehicleMapper {

    // ──────────────────────────────────────────────────────────────────────────
    // Entity -> DTO (Conversiones de Salida)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Convierte una entidad Vehicle a un DTO básico para listados.
     * Realiza un "flattening" del nombre de la marca.
     */
    public static VehicleDTO toDTO(Vehicle entity) {
        if (entity == null) return null;
        VehicleDTO dto = new VehicleDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setMatricula(entity.getMatricula());
        dto.setColor(entity.getColor());
        dto.setYear(entity.getYear());
        dto.setKm(entity.getKm());

        if (entity.getBrand() != null) {
            dto.setBrandName(entity.getBrand().getName());
        }
        return dto;
    }

    /**
     * Convierte una lista de entidades en una lista de DTOs básicos.
     */
    public static List<VehicleDTO> toDTOList(List<Vehicle> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(VehicleMapper::toDTO).toList();
    }

    /**
     * Convierte la entidad a un DTO de detalle que incluye el objeto BrandDTO completo.
     */
    public static VehicleDetailDTO toDetailDTO(Vehicle entity) {
        if (entity == null) return null;
        VehicleDetailDTO dto = new VehicleDetailDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setMatricula(entity.getMatricula());
        dto.setColor(entity.getColor());
        dto.setYear(entity.getYear());
        dto.setKm(entity.getKm());

        // Reutiliza el mapper de marcas para la composición
        dto.setBrand(BrandMapper.toDTO(entity.getBrand()));
        return dto;
    }

    /**
     * Transforma la entidad en un DTO preparado para formularios de edición.
     */
    public static VehicleUpdateDTO toUpdateDTO(Vehicle entity) {
        if (entity == null) return null;
        VehicleUpdateDTO dto = new VehicleUpdateDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setMatricula(entity.getMatricula());
        dto.setColor(entity.getColor());
        dto.setYear(entity.getYear());
        dto.setKm(entity.getKm());

        dto.setBrandId(entity.getBrand() != null ? entity.getBrand().getId() : null);
        return dto;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DTO -> Entity (Conversiones de Entrada)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Crea una nueva entidad Vehicle a partir de un DTO de creación.
     * Vincula la marca mediante una instancia proxy (solo ID).
     */
    public static Vehicle toEntity(VehicleCreateDTO dto) {
        if (dto == null) return null;
        Vehicle e = new Vehicle();
        e.setModel(dto.getModel());
        e.setMatricula(dto.getMatricula());
        e.setColor(dto.getColor());
        e.setYear(dto.getYear());
        e.setKm(dto.getKm());

        if (dto.getBrandId() != null) {
            Brand brand = new Brand();
            brand.setId(dto.getBrandId());
            e.setBrand(brand);
        }

        return e;
    }

    /**
     * Convierte un DTO de actualización en una entidad completa (incluye ID).
     */
    public static Vehicle toEntity(VehicleUpdateDTO dto) {
        if (dto == null) return null;
        Vehicle e = new Vehicle();
        e.setId(dto.getId());
        e.setModel(dto.getModel());
        e.setMatricula(dto.getMatricula());
        e.setColor(dto.getColor());
        e.setYear(dto.getYear());
        e.setKm(dto.getKm());

        if (dto.getBrandId() != null) {
            Brand brand = new Brand();
            brand.setId(dto.getBrandId());
            e.setBrand(brand);
        }

        return e;
    }

    /**
     * Copia los valores de un DTO de actualización sobre una entidad persistente existente.
     */
    public static void copyToExistingEntity(VehicleUpdateDTO dto, Vehicle entity) {
        if (dto == null || entity == null) return;

        entity.setModel(dto.getModel());
        entity.setMatricula(dto.getMatricula());
        entity.setColor(dto.getColor());
        entity.setYear(dto.getYear());
        entity.setKm(dto.getKm());

        if (dto.getBrandId() != null) {
            Brand brand = new Brand();
            brand.setId(dto.getBrandId());
            entity.setBrand(brand);
        }
    }
}