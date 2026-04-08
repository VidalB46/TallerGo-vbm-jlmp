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
     * Formulario para que el mecánico cree un presupuesto para una reparación.
     */
    @GetMapping("/new")
    public String showCreateForm(@RequestParam Long repairId, Model model) {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setRepairId(repairId);
        model.addAttribute("budget", dto);
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
     * Acción para que el cliente RECHACE el presupuesto.
     */
    @PostMapping("/{id}/reject")
    public String rejectBudget(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            budgetService.deleteBudget(id);
            redirectAttributes.addFlashAttribute("success", "Presupuesto rechazado. El mecánico tendrá que generar una nueva propuesta.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
        }
        return "redirect:/appointments";
    }
}