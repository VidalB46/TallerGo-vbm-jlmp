package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Budget;
import org.daw2.tallergo.crud_tallergo.entities.BudgetLine;
import org.daw2.tallergo.crud_tallergo.entities.Repair;

import java.util.stream.Collectors;

/**
 * Clase utilitaria para mapear datos entre las entidades de Presupuesto y sus respectivos DTOs.
 */
public class BudgetMapper {

    public static BudgetDTO toDTO(Budget entity) {
        if (entity == null) return null;

        BudgetDTO dto = new BudgetDTO();
        dto.setId(entity.getId());
        dto.setTotalGross(entity.getTotalGross());
        dto.setTotalNet(entity.getTotalNet());
        dto.setAccepted(entity.getAccepted());

        if (entity.getRepair() != null) {
            dto.setRepairId(entity.getRepair().getId());
        }
        return dto;
    }

    public static BudgetDetailDTO toDetailDTO(Budget entity) {
        if (entity == null) return null;

        BudgetDetailDTO dto = new BudgetDetailDTO();
        dto.setId(entity.getId());
        dto.setTotalGross(entity.getTotalGross());
        dto.setTotalNet(entity.getTotalNet());
        dto.setAccepted(entity.getAccepted());

        if (entity.getRepair() != null) {
            dto.setRepairId(entity.getRepair().getId());
            if (entity.getRepair().getVehicle() != null) {
                dto.setVehicleMatricula(entity.getRepair().getVehicle().getMatricula());
            }
        }

        // Mapear la lista de líneas al DTO detallado para que el cliente las vea
        if (entity.getLines() != null) {
            dto.setLines(entity.getLines().stream()
                    .map(BudgetMapper::toLineDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Mapea un DTO de creación de presupuesto a una Entidad.
     * No mapea los totales porque estos deben ser calculados por el servicio.
     */
    public static Budget toEntity(BudgetCreateDTO dto, Repair repair) {
        if (dto == null) return null;

        Budget entity = new Budget();
        entity.setAccepted(false);
        entity.setRepair(repair);

        // Transformar la lista de DTOs de líneas a Entidades
        if (dto.getLines() != null) {
            dto.getLines().forEach(lineDto -> {
                BudgetLine line = new BudgetLine();
                line.setConcept(lineDto.getConcept());
                line.setQuantity(lineDto.getQuantity());
                line.setUnitPrice(lineDto.getUnitPrice());

                // Usamos el helper de la entidad para asegurar la relación bidireccional
                entity.addLine(line);
            });
        }

        return entity;
    }

    /**
     * Mapea una entidad de línea de presupuesto a su DTO correspondiente.
     */
    public static BudgetLineDTO toLineDTO(BudgetLine entity) {
        if (entity == null) return null;

        BudgetLineDTO dto = new BudgetLineDTO();
        dto.setId(entity.getId());
        dto.setConcept(entity.getConcept());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setLineTotal(entity.getLineTotal()); // El total de la línea (Cantidad * Precio)

        return dto;
    }

    public static void updateEntity(BudgetUpdateDTO dto, Budget entity) {
        if (dto == null || entity == null) return;

        if (dto.getAccepted() != null) {
            entity.setAccepted(dto.getAccepted());
        }
    }
}