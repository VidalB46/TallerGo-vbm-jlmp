package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.ReviewCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Review;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;

/**
 * Clase utilitaria para mapear datos entre la entidad {@link Review} y sus DTOs.
 * Todos los métodos son estáticos para evitar instanciación innecesaria.
 */
public class ReviewMapper {

    /**
     * Convierte una entidad {@link Review} a un DTO resumido para listados.
     *
     * @param entity Entidad de reseña (puede ser {@code null}).
     * @return DTO resumido, o {@code null} si la entidad es {@code null}.
     */
    public static ReviewDTO toDTO(Review entity) {
        if (entity == null) return null;

        return ReviewDTO.builder()
                .id(entity.getId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .userFullName(entity.getUser() != null && entity.getUser().getProfile() != null ?
                        entity.getUser().getProfile().getFirstName() + " " + entity.getUser().getProfile().getLastName() :
                        "Usuario Anónimo")
                .workshopName(entity.getWorkshop() != null ? entity.getWorkshop().getName() : "Taller Desconocido")
                .build();
    }

    /**
     * Convierte una entidad {@link Review} a un DTO de detalle completo.
     *
     * @param entity Entidad de reseña (puede ser {@code null}).
     * @return DTO de detalle con usuario y taller incluidos, o {@code null}.
     */
    public static ReviewDetailDTO toDetailDTO(Review entity) {
        if (entity == null) return null;

        ReviewDetailDTO dto = new ReviewDetailDTO();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());

        if (entity.getUser() != null) {
            dto.setUserEmail(entity.getUser().getEmail());
            if (entity.getUser().getProfile() != null) {
                dto.setUserFullName(entity.getUser().getProfile().getFirstName() + " " + entity.getUser().getProfile().getLastName());
            }
        }

        dto.setWorkshop(WorkshopMapper.toDTO(entity.getWorkshop()));

        return dto;
    }

    /**
     * Crea una nueva entidad {@link Review} a partir del DTO y sus relaciones.
     *
     * @param dto      DTO con rating, comentario e ID del taller (puede ser {@code null}).
     * @param user     Usuario que publica la reseña.
     * @param workshop Taller valorado.
     * @return Nueva entidad de reseña, o {@code null} si el DTO es {@code null}.
     */
    public static Review toEntity(ReviewCreateDTO dto, User user, Workshop workshop) {
        if (dto == null) return null;

        Review entity = new Review();
        entity.setRating(dto.getRating());
        entity.setComment(dto.getComment());
        entity.setUser(user);
        entity.setWorkshop(workshop);

        return entity;
    }

    /**
     * Actualiza los campos modificables de una entidad {@link Review} existente.
     *
     * @param dto    DTO con los nuevos valores (campos nulos se ignoran).
     * @param entity Entidad de reseña a actualizar.
     */
    public static void updateEntity(ReviewUpdateDTO dto, Review entity) {
        if (dto == null || entity == null) return;

        if (dto.getRating() != null) {
            entity.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
    }
}