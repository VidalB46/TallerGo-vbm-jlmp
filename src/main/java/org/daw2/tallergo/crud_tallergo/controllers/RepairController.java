package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.RepairCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
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

    @GetMapping
    public String listRepairs(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<RepairDTO> repairs = repairService.getAllRepairs(pageable);
        model.addAttribute("repairsPage", repairs);
        return "views/repair/repair-list";
    }

    @GetMapping("/{id}")
    public String viewRepair(@PathVariable Long id, Model model) {
        RepairDetailDTO detail = repairService.getRepairById(id);
        model.addAttribute("repair", detail);
        return "views/repair/repair-detail";
    }

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
            redirectAttributes.addFlashAttribute("success", "Vehículo recepcionado. La reparación está en STANDBY.");
            // CORRECCIÓN: Al recepcionar, volvemos a la cita automáticamente
            return "redirect:/appointments/" + dto.getAppointmentId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/appointments";
        }
    }

    @PostMapping("/{id}/start")
    public String startRepair(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            repairService.updateStatus(id, org.daw2.tallergo.crud_tallergo.enums.RepairStatus.ACTIVO);
            redirectAttributes.addFlashAttribute("success", "¡Trabajo iniciado! El coche ya está en reparación.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }

    @PostMapping("/{id}/finish")
    public String finishRepair(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            repairService.updateStatus(id, org.daw2.tallergo.crud_tallergo.enums.RepairStatus.FINALIZADO);
            redirectAttributes.addFlashAttribute("success", "¡Reparación finalizada! El vehículo está listo para recoger.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al finalizar: " + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }

    @PostMapping("/{id}/deliver")
    public String deliverVehicle(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            repairService.deliverVehicle(id);
            redirectAttributes.addFlashAttribute("success", "¡Vehículo entregado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al entregar: " + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }
}