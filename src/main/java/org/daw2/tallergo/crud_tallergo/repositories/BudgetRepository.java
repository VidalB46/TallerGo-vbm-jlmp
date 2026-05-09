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
     * Recupera el último presupuesto activo (no rechazado) asociado a una reparación,
     * cargando también sus líneas.
     */
    @Query("""
           SELECT DISTINCT b
           FROM Budget b
           LEFT JOIN FETCH b.lines
           WHERE b.id = (
               SELECT MAX(b2.id)
               FROM Budget b2
               WHERE b2.repair.id = :repairId
                 AND b2.rejected = false
           )
           """)
    Optional<Budget> findLatestActiveByRepairId(@Param("repairId") Long repairId);

    /**
     * Comprueba si existe alguna versión aceptada y no rechazada para una reparación.
     */
    boolean existsByRepair_IdAndAcceptedTrueAndRejectedFalse(Long repairId);

    /**
     * Recupera un presupuesto cargando de forma ansiosa la reparación, la cita y las líneas vinculadas.
     */
    @Query("""
           SELECT DISTINCT b
           FROM Budget b
           JOIN FETCH b.repair r
           JOIN FETCH r.appointment
           LEFT JOIN FETCH b.lines
           WHERE b.id = :id
           """)
    Optional<Budget> findByIdWithRepair(@Param("id") Long id);
}