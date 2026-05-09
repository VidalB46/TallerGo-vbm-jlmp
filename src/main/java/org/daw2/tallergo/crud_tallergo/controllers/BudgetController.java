package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;
import org.daw2.tallergo.crud_tallergo.services.BudgetService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador MVC para la gestión de presupuestos.
 */
@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * Muestra el presupuesto activo asociado a una reparación concreta.
     * Si no existe y el usuario es ADMIN o WORKSHOP_ADMIN, redirige al formulario de creación.
     * Si no existe y es CLIENT, vuelve a citas con mensaje de error.
     */
    @GetMapping("/repair/{repairId}")
    public String viewBudgetByRepair(@PathVariable Long repairId,
                                     Model model,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            BudgetDetailDTO budget = budgetService.getBudgetByRepairId(repairId);
            model.addAttribute("budget", budget);
            return "views/budget/budget-detail";
        } catch (Exception e) {
            boolean isWorkshopSide = authentication != null &&
                    authentication.getAuthorities().stream().anyMatch(a ->
                            a.getAuthority().equals("ROLE_ADMIN") ||
                                    a.getAuthority().equals("ROLE_WORKSHOP_ADMIN"));

            if (isWorkshopSide) {
                return "redirect:/budgets/new?repairId=" + repairId;
            }

            redirectAttributes.addFlashAttribute("error", "No existe presupuesto disponible para esta reparación.");
            return "redirect:/appointments";
        }
    }

    /**
     * Muestra el formulario de presupuesto.
     * Si ya existe uno activo, precarga sus datos para permitir la modificación.
     */
    @GetMapping("/new")
    public String showCreateForm(@RequestParam Long repairId, Model model) {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setRepairId(repairId);

        try {
            BudgetDetailDTO existing = budgetService.getBudgetByRepairId(repairId);
            dto.setNotes(existing.getNotes());
            dto.setLines(existing.getLines());
        } catch (Exception ignored) {
            // Si no existe presupuesto activo, mostramos formulario vacío
        }

        model.addAttribute("budget", dto);
        return "views/budget/budget-form";
    }

    /**
     * Crea o modifica un presupuesto.
     */
    @PostMapping("/new")
    public String createBudget(@Valid @ModelAttribute("budget") BudgetCreateDTO dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("budget", dto);
            return "views/budget/budget-form";
        }

        try {
            budgetService.createBudget(dto);
            redirectAttributes.addFlashAttribute("success", "Presupuesto guardado y enviado correctamente.");
            return "redirect:/repairs/" + dto.getRepairId();
        } catch (Exception e) {
            model.addAttribute("budget", dto);
            model.addAttribute("error", e.getMessage());
            return "views/budget/budget-form";
        }
    }

    /**
     * Acción para que el cliente acepte el presupuesto.
     */
    @PostMapping("/{id}/accept")
    public String acceptBudget(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            BudgetUpdateDTO updateDTO = new BudgetUpdateDTO();
            updateDTO.setId(id);
            updateDTO.setAccepted(true);

            budgetService.updateBudget(updateDTO);

            redirectAttributes.addFlashAttribute("success", "¡Has aceptado el presupuesto! El taller podrá continuar.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo aceptar el presupuesto: " + e.getMessage());
        }
        return "redirect:/appointments";
    }

    /**
     * Acción para que el cliente rechace el presupuesto.
     * Si era el presupuesto inicial, cancela la cita.
     * Si era una modificación, mantiene el presupuesto anterior aceptado.
     */
    @PostMapping("/{id}/reject")
    public String rejectBudget(@PathVariable Long id, RedirectAttributes ra) {
        try {
            boolean isCancelled = budgetService.rejectBudget(id);

            if (isCancelled) {
                ra.addFlashAttribute("success", "Presupuesto rechazado y cita cancelada correctamente.");
            } else {
                ra.addFlashAttribute("success", "Modificación rechazada. Se mantiene el presupuesto anterior aceptado.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
        }
        return "redirect:/appointments";
    }
}