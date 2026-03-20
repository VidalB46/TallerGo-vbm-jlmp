package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.RepairCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairUpdateDTO;
import org.daw2.tallergo.crud_tallergo.services.RepairService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/repairs")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    /**
     * Listado general de reparaciones para mecánicos y admin.
     */
    @GetMapping
    public String listRepairs(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<RepairDTO> repairs = repairService.getAllRepairs(pageable);

        model.addAttribute("repairsPage", repairs);
        return "views/repair/repair-list";
    }

    /**
     * Ver el detalle técnico de una reparación.
     */
    @GetMapping("/{id}")
    public String viewRepair(@PathVariable Long id, Model model) {
        RepairDetailDTO detail = repairService.getRepairById(id);
        model.addAttribute("repair", detail);
        return "views/repair/repair-detail";
    }

    /**
     * Iniciar una reparación desde una cita previa.
     * El ID de la cita y del vehículo se pasan por parámetro.
     */
    @GetMapping("/new")
    public String showCreateForm(@RequestParam Long appointmentId,
                                 @RequestParam Long vehicleId,
                                 Model model) {
        RepairCreateDTO dto = new RepairCreateDTO();
        dto.setAppointmentId(appointmentId);
        dto.setVehicleId(vehicleId);

        model.addAttribute("repair", dto);
        return "views/repair/repair-form";
    }

    @PostMapping("/new")
    public String createRepair(@Valid @ModelAttribute("repair") RepairCreateDTO dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "views/repair/repair-form";

        try {
            repairService.createRepair(dto);
            redirectAttributes.addFlashAttribute("success", "Reparación iniciada correctamente.");
            return "redirect:/repairs";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/appointments";
        }
    }
}