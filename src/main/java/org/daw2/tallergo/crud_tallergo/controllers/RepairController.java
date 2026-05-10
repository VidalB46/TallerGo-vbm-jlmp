package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;
import org.daw2.tallergo.crud_tallergo.services.RepairService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/repairs")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;
    private final MessageSource messageSource;

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

    @PostMapping("/{id}/receive")
    public String receiveVehicle(@PathVariable Long id,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {

        Locale locale = LocaleContextHolder.getLocale();

        try {
            repairService.receiveVehicle(id);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("msg.repair.receive.success", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("msg.repair.receive.error", null, locale) + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }

    @PostMapping("/{id}/start")
    public String startRepair(@PathVariable Long id,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {

        Locale locale = LocaleContextHolder.getLocale();

        try {
            repairService.updateStatus(id, RepairStatus.ACTIVO);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("msg.repair.start.success", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("msg.repair.start.error", null, locale) + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }

    @PostMapping("/{id}/finish")
    public String finishRepair(@PathVariable Long id,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {

        Locale locale = LocaleContextHolder.getLocale();

        try {
            repairService.updateStatus(id, RepairStatus.FINALIZADO);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("msg.repair.finish.success", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("msg.repair.finish.error", null, locale) + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }

    @PostMapping("/{id}/deliver")
    public String deliverVehicle(@PathVariable Long id,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {

        Locale locale = LocaleContextHolder.getLocale();

        try {
            repairService.deliverVehicle(id);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("msg.repair.deliver.success", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("msg.repair.deliver.error", null, locale) + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/appointments");
    }
}