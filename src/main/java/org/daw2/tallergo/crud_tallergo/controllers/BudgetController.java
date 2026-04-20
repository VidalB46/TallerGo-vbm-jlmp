package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BudgetUpdateDTO;
import org.daw2.tallergo.crud_tallergo.services.BudgetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * Muestra el presupuesto asociado a una reparación concreta.
     */
    @GetMapping("/repair/{repairId}")
    public String viewBudgetByRepair(@PathVariable Long repairId, Model model) {
        try {
            BudgetDetailDTO budget = budgetService.getBudgetByRepairId(repairId);
            model.addAttribute("budget", budget);
            return "views/budget/budget-detail";
        } catch (Exception e) {
            // Si no hay presupuesto, mandamos al formulario de creación (solo mecánicos)
            return "redirect:/budgets/new?repairId=" + repairId;
        }
    }

    /**
     * Muestra el formulario de presupuesto.
     * Si ya existe uno, precarga sus datos para permitir la edición.
     */
    @GetMapping("/new")
    public String showCreateForm(@RequestParam Long repairId, Model model) {
        try {
            // Intentamos buscar si ya hay un presupuesto previo para editarlo
            BudgetDetailDTO existing = budgetService.getBudgetByRepairId(repairId);

            BudgetCreateDTO dto = new BudgetCreateDTO();
            dto.setRepairId(repairId);
            dto.setTotalGross(existing.getTotalGross());
            dto.setTotalNet(existing.getTotalNet());

            model.addAttribute("budget", dto);
        } catch (Exception e) {
            // Si no existe, enviamos un DTO vacío con el repairId
            BudgetCreateDTO dto = new BudgetCreateDTO();
            dto.setRepairId(repairId);
            model.addAttribute("budget", dto);
        }
        return "views/budget/budget-form";
    }

    @PostMapping("/new")
    public String createBudget(@Valid @ModelAttribute("budget") BudgetCreateDTO dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "views/budget/budget-form";

        budgetService.createBudget(dto);
        redirectAttributes.addFlashAttribute("success", "Presupuesto generado y enviado al cliente.");
        return "redirect:/repairs/" + dto.getRepairId();
    }

    /**
     * Acción para que el cliente ACEPTE el presupuesto.
     */
    @PostMapping("/{id}/accept")
    public String acceptBudget(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        BudgetDetailDTO budget = budgetService.getBudgetById(id);

        BudgetUpdateDTO updateDTO = new BudgetUpdateDTO();
        updateDTO.setId(id);
        updateDTO.setAccepted(true);

        budgetService.updateBudget(updateDTO);

        redirectAttributes.addFlashAttribute("success", "¡Has aceptado el presupuesto! El mecánico comenzará pronto.");
        return "redirect:/appointments";
    }

    /**
     * Acción para que el cliente RECHACE el presupuesto y cancele la cita.
     */
    @PostMapping("/{id}/reject")
    public String rejectBudget(@PathVariable Long id, RedirectAttributes ra) {
        try {
            // Guardamos el resultado del servicio
            boolean isCancelled = budgetService.rejectBudget(id);

            // Elegimos el mensaje correcto según lo que haya pasado
            if (isCancelled) {
                ra.addFlashAttribute("success", "Presupuesto rechazado y cita cancelada correctamente.");
            } else {
                ra.addFlashAttribute("success", "Modificación rechazada. El taller continuará trabajando con el presupuesto original aceptado.");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
        }
        return "redirect:/appointments";
    }
}