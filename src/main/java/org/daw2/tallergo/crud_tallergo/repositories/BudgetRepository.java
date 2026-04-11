package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Budget.
 * Gestiona la información económica asociada a las reparaciones del taller.
 */
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    /**
     * Recupera el presupuesto asociado a una reparación.
     */
    @Query("SELECT DISTINCT b FROM Budget b LEFT JOIN FETCH b.lines WHERE b.repair.id = :repairId")
    Optional<Budget> findByRepairId(@Param("repairId") Long repairId);

    /**
     * Recupera un presupuesto cargando de forma ansiosa la reparación y la cita vinculada.
     */
    @Query("SELECT b FROM Budget b JOIN FETCH b.repair r JOIN FETCH r.appointment WHERE b.id = :id")
    Optional<Budget> findByIdWithRepair(@Param("id") Long id);
}