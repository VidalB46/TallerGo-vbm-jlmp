package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;

import java.util.List;

/**
 * Mapper utilitario entre la entidad Vehicle y sus DTOs.
 * Implementación simple sin frameworks de mapeo.
 */
public class VehicleMapper {

    // ------------------------------------------------
    // Entity -> DTO (listado/tabla básico)
    // ------------------------------------------------

    /**
     * Convierte una entidad {@link Vehicle} a {@link VehicleDTO}.
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

        // Establecemos el nombre de la marca para mostrar en el listado
        if (entity.getBrand() != null) {
            dto.setBrandName(entity.getBrand().getName());
        }
        return dto;
    }

    /**
     * Convierte una lista de {@link Vehicle} a una lista de {@link VehicleDTO}.
     */
    public static List<VehicleDTO> toDTOList(List<Vehicle> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(VehicleMapper::toDTO).toList();
    }

    // ------------------------------------------------
    // Entity -> DTO (detalle con marca completa)
    // ------------------------------------------------

    /**
     * Convierte un {@link Vehicle} a {@link VehicleDetailDTO}, incluyendo la marca asociada.
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

        // Usamos el BrandMapper para convertir la marca completa
        dto.setBrand(BrandMapper.toDTO(entity.getBrand()));
        return dto;
    }

    /**
     * Convierte un {@link Vehicle} a {@link VehicleUpdateDTO} para precargar el formulario.
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

        // Asignamos el ID de la marca para que el <select> la marque
        dto.setBrandId(entity.getBrand() != null ? entity.getBrand().getId() : null);
        return dto;
    }

    // ------------------------------------------------
    // DTO (create/update) -> Entity
    // ------------------------------------------------

    /**
     * Crea una nueva entidad {@link Vehicle} desde un {@link VehicleCreateDTO}.
     */
    public static Vehicle toEntity(VehicleCreateDTO dto) {
        if (dto == null) return null;
        Vehicle e = new Vehicle();
        e.setModel(dto.getModel());
        e.setMatricula(dto.getMatricula());
        e.setColor(dto.getColor());
        e.setYear(dto.getYear());
        e.setKm(dto.getKm());

        // Creamos una marca vacía y le pasamos solo el ID
        Brand brand = new Brand();
        brand.setId(dto.getBrandId());
        e.setBrand(brand);

        return e;
    }

    /**
     * Crea una entidad {@link Vehicle} desde un {@link VehicleUpdateDTO}.
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

        Brand brand = new Brand();
        brand.setId(dto.getBrandId());
        e.setBrand(brand);

        return e;
    }

    /**
     * Copia campos editables desde un {@link VehicleUpdateDTO} a una entidad existente.
     */
    public static void copyToExistingEntity(VehicleUpdateDTO dto, Vehicle entity) {
        if (dto == null || entity == null) return;

        entity.setModel(dto.getModel());
        entity.setMatricula(dto.getMatricula());
        entity.setColor(dto.getColor());
        entity.setYear(dto.getYear());
        entity.setKm(dto.getKm());

        // Actualizamos la relación solo con el ID
        Brand brand = new Brand();
        brand.setId(dto.getBrandId());
        entity.setBrand(brand);
    }
}
