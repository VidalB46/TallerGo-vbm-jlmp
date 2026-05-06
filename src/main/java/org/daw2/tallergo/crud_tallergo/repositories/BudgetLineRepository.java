package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.BudgetLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA para la entidad {@link org.daw2.tallergo.crud_tallergo.entities.BudgetLine}.
 * Proporciona operaciones CRUD estándar y una consulta personalizada para
 * eliminar todas las líneas de un presupuesto dado.
 */
public interface BudgetLineRepository extends JpaRepository<BudgetLine, Long> {


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM BudgetLine bl WHERE bl.budget.id = :budgetId")
    /**
     * Elimina todas las líneas de presupuesto asociadas al presupuesto indicado.
     *
     * @param budgetId Identificador del presupuesto cuyas líneas se eliminarán.
     */
    void deleteAllByBudgetId(@Param("budgetId") Long budgetId);
}