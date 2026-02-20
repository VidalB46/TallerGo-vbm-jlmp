package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleUpdateDTO;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.services.BrandService;
import org.daw2.tallergo.crud_tallergo.services.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private BrandService brandService;

    @GetMapping
    public String listVehicles(
            @PageableDefault(size = 10, sort = "model", direction = Sort.Direction.ASC) Pageable pageable,
            Model model,
            Locale locale) {

        try {
            Page<VehicleDTO> page = vehicleService.list(pageable);
            model.addAttribute("page", page);

            String sortParam = "model,asc";
            if (page.getSort().isSorted()) {
                Sort.Order order = page.getSort().iterator().next();
                sortParam = order.getProperty() + "," + order.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);

        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.list.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }

        return "views/vehicle/vehicle-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        try {
            List<BrandDTO> listBrandsDTOs = brandService.listAll();
            model.addAttribute("vehicle", new VehicleCreateDTO());
            model.addAttribute("listBrands", listBrandsDTOs);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "views/vehicle/vehicle-form";
    }

    @PostMapping("/insert")
    public String insertVehicle(@Valid @ModelAttribute("vehicle") VehicleCreateDTO vehicleDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model,
                                Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("listBrands", brandService.listAll());
            return "views/vehicle/vehicle-form";
        }

        try {
            vehicleService.create(vehicleDTO);
            return "redirect:/vehicles";

        } catch (DuplicateResourceException ex) {
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/vehicles/new";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        try {
            VehicleUpdateDTO vehicleDTO = vehicleService.getForEdit(id);
            model.addAttribute("vehicle", vehicleDTO);
            model.addAttribute("listBrands", brandService.listAll());
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/vehicles";
        }
        return "views/vehicle/vehicle-form";
    }

    @PostMapping("/update")
    public String updateVehicle(@Valid @ModelAttribute("vehicle") VehicleUpdateDTO vehicleDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model,
                                Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("listBrands", brandService.listAll());
            return "views/vehicle/vehicle-form";
        }

        try {
            vehicleService.update(vehicleDTO);
            return "redirect:/vehicles";

        } catch (DuplicateResourceException ex) {
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/vehicles/edit?id=" + vehicleDTO.getId();
        }
    }

    @PostMapping("/delete")
    public String deleteVehicle(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            vehicleService.delete(id);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            VehicleDetailDTO vehicleDTO = vehicleService.getDetail(id);
            model.addAttribute("vehicle", vehicleDTO);
            return "views/vehicle/vehicle-detail";
        } catch (Exception e) {
            String msg = messageSource.getMessage("msg.vehicle-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/vehicles";
        }
    }
}