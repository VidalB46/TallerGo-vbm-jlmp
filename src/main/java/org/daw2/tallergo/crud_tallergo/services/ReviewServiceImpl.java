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

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WorkshopRepository workshopRepository;

    @Override
    @Transactional(readOnly = true)
    public ReviewDetailDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));
        return ReviewMapper.toDetailDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByWorkshop(Integer workshopId) {
        return reviewRepository.findByWorkshopId(workshopId).stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());
    }

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

    @Override
    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        reviewRepository.deleteById(id);
    }
}