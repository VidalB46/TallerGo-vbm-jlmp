package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Mechanic.
 * Gestiona el acceso a datos de los empleados técnicos de los talleres.
 */
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    /**
     * Recupera un mecánico específico cargando de forma ansiosa (Eager) su taller asociado.
     * * Al usar FETCH JOIN, evitamos el problema de las "N+1 consultas" cuando el Mapper
     * intenta acceder a m.getWorkshop().getName(), ya que toda la información se trae
     * en un único JOIN de SQL.
     * * @param id Identificador único del mecánico.
     * @return Optional con el mecánico y los datos de su taller cargados.
     */
    @Query("SELECT m FROM Mechanic m LEFT JOIN FETCH m.workshop WHERE m.id = :id")
    Optional<Mechanic> findByIdWithWorkshop(@Param("id") Long id);
}