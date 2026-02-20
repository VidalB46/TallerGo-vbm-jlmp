package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Integer> {

    // Comprobar si ya existe el nif para validaciones
    boolean existsByNif(String nif);

    // Consulta para obtener el taller con sus mecánicos asociados (JOIN FETCH)
    @Query("SELECT w FROM Workshop w LEFT JOIN FETCH w.mechanics WHERE w.id = :id")
    Optional<Workshop> findByIdWithMechanics(@Param("id") Integer id);

    // PAra cargar servicios en el futuro
    @Query("SELECT w FROM Workshop w LEFT JOIN FETCH w.mechanics WHERE w.id = :id")
    Optional<Workshop> findByIdWithServices(@Param("id") Integer id);
}