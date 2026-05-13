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

/**
 * Implementación de la lógica de negocio para la gestión de presupuestos.
 * Incluye lógica de versionado: si el cliente aún no ha aceptado el presupuesto actual,
 * se sobrescribe; si ya lo aceptó, se genera una nueva versión (v2, v3...).
 * El IVA aplicado es del 21 % sobre el total bruto de las líneas.
 */
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.21");

    private final BudgetRepository budgetRepository;
    private final RepairRepository repairRepository;
    private final BudgetLineRepository budgetLineRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Devuelve el detalle completo de un presupuesto por su ID.
     *
     * @param id Identificador único del presupuesto.
     * @return DTO de detalle con líneas, totales y estado de aceptación.
     * @throws IllegalArgumentException si no existe ningún presupuesto con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public BudgetDetailDTO getBudgetById(Long id) {
        Budget budget = budgetRepository.findByIdWithRepair(id)
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));
        return BudgetMapper.toDetailDTO(budget);
    }

    /**
     * Devuelve el detalle del presupuesto activo para una reparación concreta.
     *
     * @param repairId Identificador único de la reparación.
     * @return DTO de detalle del presupuesto activo.
     * @throws IllegalArgumentException si la reparación no existe o no tiene presupuesto activo.
     */
    @Override
    @Transactional(readOnly = true)
    public BudgetDetailDTO getBudgetByRepairId(Long repairId) {
        repairRepository.findById(repairId)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        Budget budget = budgetRepository.findLatestActiveByRepairId(repairId)
                .orElseThrow(() -> new IllegalArgumentException("No existe presupuesto activo para esta reparación"));

        return BudgetMapper.toDetailDTO(budget);
    }

    /**
     * Crea o actualiza el presupuesto de una reparación aplicando lógica de versionado.
     * Si el presupuesto actual no ha sido aceptado se sobrescribe; si ya fue aceptado
     * se genera uno nuevo. Calcula automáticamente el total bruto y el total con IVA.
     */
    @Override
    @Transactional
    public BudgetDTO createBudget(BudgetCreateDTO dto) {
        Repair repair = repairRepository.findById(dto.getRepairId())
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        Budget currentBudget = budgetRepository.findLatestActiveByRepairId(repair.getId()).orElse(null);
        Budget budget;

        if (currentBudget != null && !Boolean.TRUE.equals(currentBudget.getAccepted())) {
            budget = currentBudget;
            budget.setNotes(dto.getNotes());
            budget.setRejected(false);
            budget.getLines().clear();
        } else {
            budget = new Budget();
            budget.setRepair(repair);
            budget.setNotes(dto.getNotes());
            budget.setAccepted(false);
            budget.setRejected(false);
        }

        BigDecimal totalGross = BigDecimal.ZERO;

        if (dto.getLines() != null) {
            for (var lineDto : dto.getLines()) {
                BudgetLine line = new BudgetLine();
                line.setConcept(lineDto.getConcept());
                line.setQuantity(lineDto.getQuantity());
                line.setUnitPrice(lineDto.getUnitPrice());

                budget.addLine(line);
                totalGross = totalGross.add(line.getLineTotal());
            }
        }

        BigDecimal taxAmount = totalGross.multiply(TAX_RATE);
        BigDecimal totalNet = totalGross.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        budget.setTotalGross(totalGross);
        budget.setTotalNet(totalNet);

        Budget saved = budgetRepository.save(budget);
        return BudgetMapper.toDTO(saved);
    }

    /**
     * Actualiza los campos editables de un presupuesto.
     *
     * @param dto DTO con los nuevos valores y el ID del presupuesto.
     * @return DTO actualizado del presupuesto.
     * @throws IllegalArgumentException si no existe ningún presupuesto con ese ID.
     */
    @Override
    @Transactional
    public BudgetDTO updateBudget(BudgetUpdateDTO dto) {
        Budget budget = budgetRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));
        BudgetMapper.updateEntity(dto, budget);
        return BudgetMapper.toDTO(budgetRepository.save(budget));
    }

    /**
     * Elimina un presupuesto por su ID.
     *
     * @param id Identificador único del presupuesto a eliminar.
     */
    @Override
    @Transactional
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    /**
     * Marca un presupuesto como rechazado por el cliente.
     * Si no existe ninguna versión aceptada anterior, cancela la cita asociada.
     *
     * @param id Identificador único del presupuesto a rechazar.
     * @return true si la cita ha sido cancelada; false si solo se rechazó el anexo.
     * @throws IllegalArgumentException si no existe ningún presupuesto con ese ID.
     */
    @Override
    @Transactional
    public boolean rejectBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Presupuesto no encontrado"));

        budget.setRejected(true);
        budgetRepository.save(budget);

        Repair repair = budget.getRepair();

        boolean hasAcceptedVersion = budgetRepository
                .existsByRepair_IdAndAcceptedTrueAndRejectedFalse(repair.getId());

        if (!hasAcceptedVersion) {
            if (repair.getAppointment() != null) {
                repair.getAppointment().setStatus(AppointmentStatus.CANCELADO);
            }
            return true;
        }

        return false;
    }
}