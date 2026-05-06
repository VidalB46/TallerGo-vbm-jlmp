package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.ReviewCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDetailDTO;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de reseñas de talleres.
 * Permite a los usuarios autenticados publicar valoraciones y a los administradores
 * eliminar contenido inapropiado.
 */
public interface ReviewService {

    /**
     * Devuelve el detalle completo de una reseña por su ID.
     *
     * @param id Identificador único de la reseña.
     * @return DTO de detalle con usuario y taller asociados.
     */
    ReviewDetailDTO getReviewById(Long id);

    /**
     * Devuelve todas las reseñas de un taller concreto.
     *
     * @param workshopId Identificador del taller.
     * @return Lista de DTOs de reseña del taller.
     */
    List<ReviewDTO> getReviewsByWorkshop(Integer workshopId);

    /**
     * Crea una nueva reseña a nombre del usuario autenticado.
     *
     * @param dto       DTO con la puntuación, comentario e ID del taller.
     * @param userEmail Email del usuario autenticado que publica la reseña.
     * @return DTO de la reseña creada.
     */
    ReviewDTO createReview(ReviewCreateDTO dto, String userEmail);

    /**
     * Elimina una reseña por su ID.
     *
     * @param id Identificador único de la reseña a eliminar.
     */
    void deleteReview(Long id);
}