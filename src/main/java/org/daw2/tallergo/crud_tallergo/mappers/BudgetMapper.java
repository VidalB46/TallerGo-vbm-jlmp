package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Budget;
import org.daw2.tallergo.crud_tallergo.entities.Repair;

public class BudgetMapper {

    public static BudgetDTO toDTO(Budget entity) {
        if (entity == null) return null;

        BudgetDTO dto = new BudgetDTO();
        dto.setId(entity.getId());
        dto.setTotalGross(entity.getTotalGross());
        dto.setTotalNet(entity.getTotalNet());

        // Para que pase el estado real
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
        return dto;
    }

    public static Budget toEntity(BudgetCreateDTO dto, Repair repair) {
        if (dto == null) return null;

        Budget entity = new Budget();
        entity.setTotalGross(dto.getTotalGross());
        entity.setTotalNet(dto.getTotalNet());
        entity.setAccepted(false);
        entity.setRepair(repair);

        return entity;
    }

    public static void updateEntity(BudgetUpdateDTO dto, Budget entity) {
        if (dto == null || entity == null) return;

        if (dto.getTotalGross() != null) {
            entity.setTotalGross(dto.getTotalGross());
        }
        if (dto.getTotalNet() != null) {
            entity.setTotalNet(dto.getTotalNet());
        }
        if (dto.getAccepted() != null) {
            entity.setAccepted(dto.getAccepted());
        }
    }
}