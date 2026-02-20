package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    @Query("SELECT m FROM Mechanic m LEFT JOIN FETCH m.workshop WHERE m.id = :id")
    Optional<Mechanic> findByIdWithWorkshop(@Param("id") Long id);
}