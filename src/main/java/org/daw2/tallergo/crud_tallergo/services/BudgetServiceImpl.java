package org.daw2.tallergo.crud_tallergo.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Budget;
import org.daw2.tallergo.crud_tallergo.entities.BudgetLine;
import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public BudgetDetailDTO getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));
        return BudgetMapper.toDetailDTO(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetDetailDTO getBudgetByRepairId(Long repairId) {
        Repair repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        Budget budget = repair.getBudget(); // Usa el truco de la versión más reciente
        if (budget == null) {
            throw new IllegalArgumentException("No existe presupuesto activo para esta reparación");
        }
        return BudgetMapper.toDetailDTO(budget);
    }

    @Override
    @Transactional
    public BudgetDTO createBudget(BudgetCreateDTO dto) {
        Repair repair = repairRepository.findById(dto.getRepairId())
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        Budget currentBudget = repair.getBudget();
        Budget budget;

        // LÓGICA DE VERSIONADO
        if (currentBudget != null && !Boolean.TRUE.equals(currentBudget.getAccepted())) {
            // El mecánico está editando un presupuesto que el cliente AÚN NO HA VISTO ni aceptado.
            // Actualizamos el mismo.
            budget = currentBudget;
            budget.setNotes(dto.getNotes());
            budget = budgetRepository.save(budget);
            budgetRepository.flush();

            budgetLineRepository.deleteAllByBudgetId(budget.getId());
            entityManager.clear();
            budget = budgetRepository.findById(budget.getId()).orElseThrow();

        } else {
            // No había presupuesto, o el anterior YA ESTABA ACEPTADO (Crear v2, v3...)
            budget = new Budget();
            budget.setRepair(repair);
            budget.setNotes(dto.getNotes());
            budget.setAccepted(false);
            budget.setRejected(false);

            budget = budgetRepository.save(budget);
            budgetRepository.flush();
        }

        // Cargar las nuevas líneas
        BigDecimal totalGross = BigDecimal.ZERO;
        if (dto.getLines() != null) {
            for (var lineDto : dto.getLines()) {
                BudgetLine line = new BudgetLine();
                line.setConcept(lineDto.getConcept());
                line.setQuantity(lineDto.getQuantity());
                line.setUnitPrice(lineDto.getUnitPrice());
                line.setBudget(budget);

                budgetLineRepository.save(line);
                totalGross = totalGross.add(line.getLineTotal());
            }
        }

        BigDecimal taxAmount = totalGross.multiply(TAX_RATE);
        BigDecimal totalNet = totalGross.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        budget.setTotalGross(totalGross);
        budget.setTotalNet(totalNet);

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
        budgetRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean rejectBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));

        // 1. Lo marcamos como rechazado
        budget.setRejected(true);
        budgetRepository.save(budget);

        Repair repair = budget.getRepair();

        // 2. Buscamos si hay alguna versión anterior aceptada
        boolean hasAcceptedVersion = repair.getBudgets().stream()
                .anyMatch(b -> Boolean.TRUE.equals(b.getAccepted()) && !Boolean.TRUE.equals(b.getRejected()));

        // 3. Si no hay versión aceptada, se cancela la cita.
        if (!hasAcceptedVersion) {
            if (repair.getAppointment() != null) {
                repair.getAppointment().setStatus(AppointmentStatus.CANCELADO);
            }
            return true; // true = La cita se ha cancelado
        }

        return false; // false = Solo hemos rechazado el anexo, la cita sigue
    }
}