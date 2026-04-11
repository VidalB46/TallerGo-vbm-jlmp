package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Budget;
import org.daw2.tallergo.crud_tallergo.entities.BudgetLine;
import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus; // IMPORTANTE: Importamos el Enum de Estados
import org.daw2.tallergo.crud_tallergo.mappers.BudgetMapper;
import org.daw2.tallergo.crud_tallergo.repositories.BudgetLineRepository;
import org.daw2.tallergo.crud_tallergo.repositories.BudgetRepository;
import org.daw2.tallergo.crud_tallergo.repositories.RepairRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.21");

    private final BudgetRepository budgetRepository;
    private final RepairRepository repairRepository;
    private final BudgetLineRepository budgetLineRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("No existe presupuesto para esta reparación"));
        return BudgetMapper.toDetailDTO(budget);
    }

    @Override
    @Transactional
    public BudgetDTO createBudget(BudgetCreateDTO dto) {
        Repair repair = repairRepository.findById(dto.getRepairId())
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        // 1. Obtenemos el presupuesto o creamos uno vacío
        Budget budget = budgetRepository.findByRepairId(repair.getId()).orElse(new Budget());
        budget.setRepair(repair);

        // Si es nuevo, esto le dará una ID. Si es viejo, no pasa nada.
        budget = budgetRepository.save(budget);

        // 2. ELIMINACIÓN DE LAS LÍNEAS ANTERIORES
        budgetLineRepository.deleteAllByBudgetId(budget.getId());

        // 3. CREACIÓN EXPLÍCITA DE LAS NUEVAS LÍNEAS
        BigDecimal totalGross = BigDecimal.ZERO;
        if (dto.getLines() != null) {
            for (var lineDto : dto.getLines()) {
                BudgetLine line = new BudgetLine();
                line.setConcept(lineDto.getConcept());
                line.setQuantity(lineDto.getQuantity());
                line.setUnitPrice(lineDto.getUnitPrice());
                line.setBudget(budget); // Le decimos quién es el padre

                // GUARDAMOS LA LÍNEA MANUALMENTE
                budgetLineRepository.save(line);

                totalGross = totalGross.add(line.getLineTotal());
            }
        }

        // 4. Actualizamos el total general del padre
        BigDecimal taxAmount = totalGross.multiply(TAX_RATE);
        BigDecimal totalNet = totalGross.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        budget.setTotalGross(totalGross);
        budget.setTotalNet(totalNet);
        budget.setAccepted(false);

        // 5. Guardamos el presupuesto final
        return BudgetMapper.toDTO(budgetRepository.save(budget));
    }

    @Override
    @Transactional
    public BudgetDTO updateBudget(BudgetUpdateDTO dto) {
        Budget budget = budgetRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));
        BudgetMapper.updateEntity(dto, budget);
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

    // NUEVO MÉTODO: Rechazar y Cancelar Cita
    @Override
    @Transactional
    public void rejectBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));

        // 1. Buscamos la cita asociada y le cambiamos el estado a CANCELADO
        if (budget.getRepair() != null && budget.getRepair().getAppointment() != null) {
            budget.getRepair().getAppointment().setStatus(AppointmentStatus.CANCELADO);
        }

        // 2. Destruimos las líneas usando el repositorio anti-zombis
        budgetLineRepository.deleteAllByBudgetId(budget.getId());

        // 3. Destruimos el presupuesto
        budgetRepository.delete(budget);
    }
}