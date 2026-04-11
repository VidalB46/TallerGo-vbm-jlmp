package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.BudgetLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetLineRepository extends JpaRepository<BudgetLine, Long> {


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM BudgetLine bl WHERE bl.budget.id = :budgetId")
    void deleteAllByBudgetId(@Param("budgetId") Long budgetId);
}