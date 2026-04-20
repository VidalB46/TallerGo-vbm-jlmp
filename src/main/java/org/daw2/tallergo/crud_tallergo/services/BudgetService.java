package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;

/**
 * Interfaz para los servicios de gestión de presupuestos.
 * Define las operaciones CRUD y las reglas de negocio para el versionado
 * de presupuestos (aceptación, modificaciones y rechazos).
 */
public interface BudgetService {

    /**
     * Obtiene un presupuesto específico por su ID.
     */
    BudgetDetailDTO getBudgetById(Long id);

    /**
     * Obtiene el presupuesto ACTIVO asociado a una reparación.
     * En un sistema versionado, devuelve el más reciente que no haya sido rechazado.
     */
    BudgetDetailDTO getBudgetByRepairId(Long repairId);

    /**
     * Crea un nuevo presupuesto o una nueva versión (v2, v3...) si el anterior
     * ya estaba aceptado y el taller añade modificaciones.
     */
    BudgetDTO createBudget(BudgetCreateDTO dto);

    /**
     * Actualiza datos básicos de un presupuesto (como su estado de aceptación).
     */
    BudgetDTO updateBudget(BudgetUpdateDTO dto);

    /**
     * Elimina un presupuesto permanentemente de la base de datos.
     */
    void deleteBudget(Long id);

    /**
     * Rechaza un presupuesto. Si es una versión modificada (ej: v2), la oculta
     * y el sistema vuelve automáticamente a la versión anterior (v1).
     * Si es el presupuesto inicial, cancela la cita.
     */
    boolean rejectBudget(Long id);
}