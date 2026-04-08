package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.ReviewDTO;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopDetailDTO;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.services.ReviewService;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final WorkshopService workshopService;
    private final UserRepository userRepository;

    @GetMapping("/new")
    public String showReviewForm(@RequestParam Integer workshopId, Model model) {
        ReviewCreateDTO dto = new ReviewCreateDTO();
        dto.setWorkshopId(workshopId);

        WorkshopDetailDTO workshop = workshopService.getDetail(workshopId);

        model.addAttribute("review", dto);
        model.addAttribute("workshop", workshop);
        return "views/review/review-form";
    }

    @PostMapping("/new")
    public String createReview(@Valid @ModelAttribute("review") ReviewCreateDTO dto,
                               BindingResult result,
                               Authentication authentication,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("workshop", workshopService.getDetail(dto.getWorkshopId()));
            return "views/review/review-form";
        }

        try {
            // Buscamos el usuario para obtener su ID
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            dto.setUserId(user.getId());


            reviewService.createReview(dto, authentication.getName());

            redirectAttributes.addFlashAttribute("success", "¡Gracias por tu reseña!");
            return "redirect:/workshops";

        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("workshop", workshopService.getDetail(dto.getWorkshopId()));
            return "views/review/review-form";
        }
    }

    @GetMapping("/workshop/{workshopId}")
    public String listWorkshopReviews(@PathVariable Integer workshopId, Model model) {
        List<ReviewDTO> reviews = reviewService.getReviewsByWorkshop(workshopId);
        WorkshopDetailDTO workshop = workshopService.getDetail(workshopId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("workshop", workshop);
        return "views/review/review-list";
    }
}