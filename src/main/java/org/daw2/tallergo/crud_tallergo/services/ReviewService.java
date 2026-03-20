package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.ReviewCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDetailDTO;

import java.util.List;

public interface ReviewService {
    ReviewDetailDTO getReviewById(Long id);
    List<ReviewDTO> getReviewsByWorkshop(Integer workshopId);
    ReviewDTO createReview(ReviewCreateDTO dto, String userEmail);
    void deleteReview(Long id);
}