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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para la gestión de reseñas de talleres.
 * Permite crear, listar y eliminar valoraciones dejadas por los clientes.
 */
@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final WorkshopService workshopService;
    private final UserRepository userRepository;

    /**
     * Muestra el formulario para escribir una nueva reseña sobre un taller.
     * Solo accesible para usuarios con rol de cliente.
     *
     * @param workshopId ID del taller sobre el que se quiere dejar la reseña.
     * @param model      Modelo para pasar el DTO vacío y los datos del taller.
     * @return Vista del formulario de reseña.
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/new")
    public String showReviewForm(@RequestParam Integer workshopId, Model model) {
        ReviewCreateDTO dto = new ReviewCreateDTO();
        dto.setWorkshopId(workshopId);

        WorkshopDetailDTO workshop = workshopService.getDetail(workshopId);

        model.addAttribute("review", dto);
        model.addAttribute("workshop", workshop);
        return "views/review/review-form";
    }

    /**
     * Procesa el formulario y guarda la reseña a nombre del usuario autenticado.
     * Solo accesible para usuarios con rol de cliente.
     *
     * @param dto                DTO con la puntuación, comentario e ID del taller.
     * @param result             Resultado de la validación del formulario.
     * @param authentication     Información del usuario autenticado.
     * @param model              Modelo para recargar datos del taller en caso de error.
     * @param redirectAttributes Atributos flash para el mensaje de éxito.
     * @return Redirección al listado de talleres o recarga del formulario si hay errores.
     */
    @PreAuthorize("hasRole('CLIENT')")
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

    /**
     * Lista todas las reseñas de un taller concreto.
     * Accesible para cualquier usuario autenticado.
     *
     * @param workshopId ID del taller cuyas reseñas se quieren visualizar.
     * @param model      Modelo para pasar la lista de reseñas y los datos del taller.
     * @return Vista del listado de reseñas del taller.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/workshop/{workshopId}")
    public String listWorkshopReviews(@PathVariable Integer workshopId, Model model) {
        List<ReviewDTO> reviews = reviewService.getReviewsByWorkshop(workshopId);
        WorkshopDetailDTO workshop = workshopService.getDetail(workshopId);

        model.addAttribute("reviews", reviews);
        model.addAttribute("workshop", workshop);
        return "views/review/review-list";
    }

    /**
     * Lista las reseñas del taller asociado al usuario autenticado con rol WORKSHOP_ADMIN.
     *
     * @param authentication     Información del usuario autenticado.
     * @param model              Modelo para pasar la lista de reseñas y los datos del taller.
     * @param redirectAttributes Atributos flash para mensajes de error.
     * @return Vista del listado de reseñas o redirección si no tiene taller asociado.
     */
    @PreAuthorize("hasRole('WORKSHOP_ADMIN')")
    @GetMapping("/my")
    public String listMyWorkshopReviews(Authentication authentication,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        try {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            if (user.getWorkshop() == null) {
                redirectAttributes.addFlashAttribute("error", "No tienes ningún taller asignado.");
                return "redirect:/appointments";
            }

            Integer workshopId = user.getWorkshop().getId();
            List<ReviewDTO> reviews = reviewService.getReviewsByWorkshop(workshopId);
            WorkshopDetailDTO workshop = workshopService.getDetail(workshopId);

            model.addAttribute("reviews", reviews);
            model.addAttribute("workshop", workshop);

            return "views/review/review-list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudieron cargar tus reseñas: " + e.getMessage());
            return "redirect:/appointments";
        }
    }

    /**
     * Elimina una reseña si el usuario autenticado es su propietario o si es administrador.
     *
     * @param id                 Identificador de la reseña a eliminar.
     * @param authentication     Información del usuario autenticado.
     * @param redirectAttributes Atributos flash para mostrar mensajes en la vista.
     * @return Redirección al listado de reseñas del taller correspondiente o a talleres.
     */
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
            Integer workshopId = reviewService.deleteReview(id, authentication.getName(), isAdmin);

            redirectAttributes.addFlashAttribute("success", "Reseña eliminada correctamente.");
            return "redirect:/reviews/workshop/" + workshopId;

        } catch (AccessDeniedException e) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar esta reseña.");
            return "redirect:/workshops";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/workshops";
        }
    }

    /**
     * Comprueba si el usuario autenticado posee un rol concreto.
     *
     * @param authentication Información de autenticación actual.
     * @param role           Nombre completo del rol, por ejemplo ROLE_ADMIN.
     * @return true si el usuario tiene el rol indicado; false en caso contrario.
     */
    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}