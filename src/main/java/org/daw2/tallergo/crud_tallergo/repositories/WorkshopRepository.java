package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Workshop.
 * Gestiona la persistencia de los talleres y la recuperación eficiente de sus relaciones
 * mediante consultas personalizadas para evitar el LazyInitializationException.
 */
@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Integer> {

    /**
     * Verifica la existencia de un taller mediante su NIF.
     * Fundamental para evitar duplicados legales durante el registro.
     */
    boolean existsByNif(String nif);

    /**
     * Recupera un taller cargando ansiosamente (Eager) su colección de mecánicos.
     * Utiliza un LEFT JOIN FETCH para minimizar el número de consultas a la base de datos
     * al generar el DTO de detalle del taller.
     * * @param id Identificador único del taller.
     * @return Optional con el taller y sus mecánicos cargados.
     */
    @Query("SELECT w FROM Workshop w LEFT JOIN FETCH w.mechanics WHERE w.id = :id")
    Optional<Workshop> findByIdWithMechanics(@Param("id") Integer id);

    /**
     * Recupera un taller cargando ansiosamente su relación con los servicios ofrecidos.
     * Nota: En el futuro, se debe ajustar el FETCH hacia 'workshopServices' y 'service'
     * para obtener la información de precios y nombres de servicios en una sola pasada.
     * * @param id Identificador único del taller.
     * @return Optional con el taller y sus servicios asociados.
     */
    @Query("SELECT w FROM Workshop w LEFT JOIN FETCH w.workshopServices ws JOIN FETCH ws.service WHERE w.id = :id")
    Optional<Workshop> findByIdWithServices(@Param("id") Integer id);
}