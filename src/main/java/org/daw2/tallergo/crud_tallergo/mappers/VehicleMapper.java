package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;

import java.util.List;

public class VehicleMapper {

    // ------------------------------------------------
    // Entity -> DTO (listado/tabla básico)
    // ------------------------------------------------

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


    public static List<VehicleDTO> toDTOList(List<Vehicle> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(VehicleMapper::toDTO).toList();
    }

    // ------------------------------------------------
    // Entity -> DTO (detalle con marca completa)
    // ------------------------------------------------

    public static VehicleDetailDTO toDetailDTO(Vehicle entity) {
        if (entity == null) return null;
        VehicleDetailDTO dto = new VehicleDetailDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setMatricula(entity.getMatricula());
        dto.setColor(entity.getColor());
        dto.setYear(entity.getYear());
        dto.setKm(entity.getKm());

        dto.setBrand(BrandMapper.toDTO(entity.getBrand()));
        return dto;
    }

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

    // ------------------------------------------------
    // DTO (create/update) -> Entity
    // ------------------------------------------------

    public static Vehicle toEntity(VehicleCreateDTO dto) {
        if (dto == null) return null;
        Vehicle e = new Vehicle();
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

    public static void copyToExistingEntity(VehicleUpdateDTO dto, Vehicle entity) {
        if (dto == null || entity == null) return;

        entity.setModel(dto.getModel());
        entity.setMatricula(dto.getMatricula());
        entity.setColor(dto.getColor());
        entity.setYear(dto.getYear());
        entity.setKm(dto.getKm());

        Brand brand = new Brand();
        brand.setId(dto.getBrandId());
        entity.setBrand(brand);
    }
}
