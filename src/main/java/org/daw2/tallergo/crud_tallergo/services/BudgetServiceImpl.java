package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Budget;
import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.daw2.tallergo.crud_tallergo.mappers.BudgetMapper;
import org.daw2.tallergo.crud_tallergo.repositories.BudgetRepository;
import org.daw2.tallergo.crud_tallergo.repositories.RepairRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final RepairRepository repairRepository;

    @Override
    @Transactional(readOnly = true)
    public BudgetDetailDTO getBudgetById(Long id) {
        Budget budget = budgetRepository.findByIdWithRepair(id)
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));
        return BudgetMapper.toDetailDTO(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetDetailDTO getBudgetByRepairId(Long repairId) {
        Budget budget = budgetRepository.findByRepairId(repairId)
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado para esta reparación"));
        return BudgetMapper.toDetailDTO(budget);
    }

    @Override
    @Transactional
    public BudgetDTO createBudget(BudgetCreateDTO dto) {
        Repair repair = repairRepository.findById(dto.getRepairId())
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        Budget budget = BudgetMapper.toEntity(dto, repair);
        return BudgetMapper.toDTO(budgetRepository.save(budget));
    }

    @Override
    @Transactional
    public BudgetDTO updateBudget(BudgetUpdateDTO dto) {
        Budget budget = budgetRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));

        BudgetMapper.updateEntity(dto, budget);
        // Guardamos el presupuesto.
        return BudgetMapper.toDTO(budgetRepository.save(budget));
    }

    @Override
    @Transactional
    public void deleteBudget(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new IllegalArgumentException("Presupuesto no encontrado");
        }
        budgetRepository.deleteById(id);
    }
}