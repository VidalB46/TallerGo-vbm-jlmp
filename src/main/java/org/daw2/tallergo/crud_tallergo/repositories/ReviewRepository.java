package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Review.
 * Gestiona las opiniones y calificaciones de los usuarios sobre los talleres.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Recupera todas las reseñas de un taller específico.
     *
     * @param workshopId Identificador del taller.
     * @return Lista de reseñas del taller indicado.
     */
    List<Review> findByWorkshopId(Integer workshopId);

    /**
     * Obtiene la nota media (rating) de un taller.
     *
     * @param workshopId Identificador del taller.
     * @return Media de puntuaciones o {@code null} si no existen reseñas.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.workshop.id = :workshopId")
    Double getAverageRatingForWorkshop(@Param("workshopId") Integer workshopId);

    /**
     * Recupera una reseña concreta cargando además el usuario autor y el taller asociado.
     * Este método resulta útil para validar permisos de borrado y conocer el taller al que
     * pertenece la reseña antes de eliminarla.
     *
     * @param id Identificador de la reseña.
     * @return Reseña encontrada con usuario y taller inicializados.
     */
    @Query("""
           SELECT r
           FROM Review r
           JOIN FETCH r.user u
           JOIN FETCH r.workshop w
           WHERE r.id = :id
           """)
    Optional<Review> findByIdWithUserAndWorkshop(@Param("id") Long id);
}