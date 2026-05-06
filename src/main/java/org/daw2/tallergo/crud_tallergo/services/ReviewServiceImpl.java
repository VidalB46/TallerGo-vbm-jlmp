package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDetailDTO;
import org.daw2.tallergo.crud_tallergo.entities.Review;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import org.daw2.tallergo.crud_tallergo.mappers.ReviewMapper;
import org.daw2.tallergo.crud_tallergo.repositories.ReviewRepository;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.repositories.WorkshopRepository;
import org.daw2.tallergo.crud_tallergo.services.ReviewService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la lógica de negocio para la gestión de reseñas de talleres.
 * Permite a los usuarios autenticados crear y consultar valoraciones sobre los talleres.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;

    /**
     * Devuelve el detalle completo de una reseña por su ID.
     *
     * @param id Identificador único de la reseña.
     * @return DTO de detalle con usuario y taller asociados.
     * @throws IllegalArgumentException si no existe ninguna reseña con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public ReviewDetailDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));
        return ReviewMapper.toDetailDTO(review);
    }

    /**
     * Devuelve todas las reseñas de un taller concreto.
     *
     * @param workshopId Identificador del taller.
     * @return Lista de DTOs de reseña del taller indicado.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByWorkshop(Integer workshopId) {
        return reviewRepository.findByWorkshopId(workshopId).stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva reseña para un taller a nombre del usuario autenticado.
     *
     * @param dto       DTO con la puntuación, comentario e ID del taller.
     * @param userEmail Email del usuario autenticado que publica la reseña.
     * @return DTO de la reseña recién creada.
     * @throws UsernameNotFoundException si no se encuentra el usuario por su email.
     * @throws IllegalArgumentException  si el taller no existe.
     */
    @Override
    @Transactional
    public ReviewDTO createReview(ReviewCreateDTO dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Workshop workshop = workshopRepository.findById(dto.getWorkshopId())
                .orElseThrow(() -> new IllegalArgumentException("Taller no encontrado"));

        Review review = ReviewMapper.toEntity(dto, user, workshop);
        return ReviewMapper.toDTO(reviewRepository.save(review));
    }

    /**
     * Elimina una reseña por su ID.
     *
     * @param id Identificador único de la reseña a eliminar.
     * @throws IllegalArgumentException si no existe ninguna reseña con ese ID.
     */
    @Override
    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        reviewRepository.deleteById(id);
    }
}