package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Spring Data JPA para la entidad Review.
 * Gestiona las opiniones y calificaciones de los usuarios sobre los talleres.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Recupera todas las reseñas de un taller específico.
     * Útil para calcular la nota media y mostrar el carrusel de opiniones.
     */
    List<Review> findByWorkshopId(Integer workshopId);

    /**
     * Obtiene la nota media (rating) de un taller.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.workshop.id = :workshopId")
    Double getAverageRatingForWorkshop(@Param("workshopId") Integer workshopId);
}