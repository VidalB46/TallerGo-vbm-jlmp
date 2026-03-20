package org.daw2.tallergo.crud_tallergo.mappers;

import org.daw2.tallergo.crud_tallergo.dtos.ReviewCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Review;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;

public class ReviewMapper {

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

    public static Review toEntity(ReviewCreateDTO dto, User user, Workshop workshop) {
        if (dto == null) return null;

        Review entity = new Review();
        entity.setRating(dto.getRating());
        entity.setComment(dto.getComment());
        entity.setUser(user);
        entity.setWorkshop(workshop);

        return entity;
    }

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