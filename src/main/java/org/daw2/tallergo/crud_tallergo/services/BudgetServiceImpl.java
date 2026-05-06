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
        Budget budget = budgetRepository.findById(id)
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
        Repair repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        Budget budget = repair.getBudget(); // Usa el truco de la versión más reciente
        if (budget == null) {
            throw new IllegalArgumentException("No existe presupuesto activo para esta reparación");
        }
        return BudgetMapper.toDetailDTO(budget);
    }

    /**
     * Crea o actualiza el presupuesto de una reparación aplicando lógica de versionado.
     * Si el presupuesto actual no ha sido aceptado se sobrescribe; si ya fue aceptado
     * se genera uno nuevo. Calcula automáticamente el total bruto y el total con IVA.
     *
     * @param dto DTO con las líneas y notas del nuevo presupuesto.
     * @return DTO del presupuesto creado o actualizado.
     * @throws IllegalArgumentException si no existe la reparación indicada.
     */
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

    /**
     * Actualiza los campos editables de un presupuesto (p. ej. notas o estado).
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
     * @return {@code true} si la cita ha sido cancelada; {@code false} si solo se rechazó
     *         el anexo y la cita continúa activa.
     * @throws IllegalArgumentException si no existe ningún presupuesto con ese ID.
     */
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
