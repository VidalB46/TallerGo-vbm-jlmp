package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.services.RepairService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador web para la gestión de las reparaciones del taller.
 * En la Fase 2, se eliminan los formularios manuales de creación y se confía en la automatización.
 */
@Controller
@RequestMapping("/repairs")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    /**
     * Muestra el listado paginado de todas las reparaciones activas e históricas.
     */
    @GetMapping
    public String listRepairs(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<RepairDTO> repairs = repairService.getAllRepairs(pageable);
        model.addAttribute("repairsPage", repairs);
        return "views/repair/repair-list";
    }

    /**
     * Muestra la vista detallada de una reparación concreta.
     */
    @GetMapping("/{id}")
    public String viewRepair(@PathVariable Long id, Model model) {
        RepairDetailDTO detail = repairService.getRepairById(id);
        model.addAttribute("repair", detail);
        return "views/repair/repair-detail";
    }

    // --- ACCIONES RÁPIDAS DE ESTADO ---

    /**
     * Recepciona el vehículo físicamente en el taller.
     * Actualiza la fecha de entrada y el estado general.
     */
    @PostMapping("/{id}/receive")
    public String receiveVehicle(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            repairService.receiveVehicle(id);
            redirectAttributes.addFlashAttribute("success", "El vehículo ha sido recepcionado en las instalaciones. El estado cambia a EN TALLER.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al recepcionar: " + e.getMessage());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }

    /**
     * Inicia el trabajo mecánico en el vehículo.
     */
    @PostMapping("/{id}/start")
    public String startRepair(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            repairService.updateStatus(id, org.daw2.tallergo.crud_tallergo.enums.RepairStatus.ACTIVO);
            redirectAttributes.addFlashAttribute("success", "¡Trabajo iniciado! El coche ya está siendo reparado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }

    /**
     * Finaliza la intervención mecánica.
     */
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

    /**
     * Entrega las llaves y el vehículo de vuelta al cliente.
     */
    @PostMapping("/{id}/deliver")
    public String deliverVehicle(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            repairService.deliverVehicle(id);
            redirectAttributes.addFlashAttribute("success", "¡Vehículo entregado al cliente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al entregar: " + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }
}