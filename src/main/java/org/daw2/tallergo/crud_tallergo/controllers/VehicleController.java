package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
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
import org.springframework.security.core.Authentication; // Importado
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

/**
 * Controlador para la gestión de vehículos.
 * Permite listar, crear, editar, actualizar, eliminar y ver detalles de vehículos.
 */
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

    @Autowired
    private UserRepository userRepository; // Movido arriba con el resto de dependencias

    /**
     * Lista los vehículos filtrando por rol: el Administrador ve todos,
     * mientras que el Cliente solo visualiza los de su propiedad.
     */
    @GetMapping
    public String listVehicles(
            @PageableDefault(size = 10, sort = "model", direction = Sort.Direction.ASC) Pageable pageable,
            Model model,
            Locale locale,
            Authentication authentication) { // Limpiado el parámetro

        try {
            // Obtener el usuario actual
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Comprobar si es ADMIN
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            Page<VehicleDTO> page;
            if (isAdmin) {
                page = vehicleService.list(pageable);
            } else {
                page = vehicleService.listByUser(user.getId(), pageable);
            }

            model.addAttribute("page", page);

            // Lógica de ordenamiento
            String sortParam = "model,asc";
            if (page.getSort().isSorted()) {
                Sort.Order order = page.getSort().iterator().next();
                sortParam = order.getProperty() + "," + order.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);

        } catch (Exception e) {
            logger.error("Error al listar vehículos: ", e);
            model.addAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.list.error", null, locale));
        }

        return "views/vehicle/vehicle-list";
    }

    // --- EL RESTO DE MÉTODOS SE MANTIENEN IGUAL ---

    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        try {
            model.addAttribute("vehicle", new VehicleCreateDTO());
            model.addAttribute("listBrands", brandService.listAll());
        } catch (Exception e) {
            model.addAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.edit.error", null, locale));
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
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.insert.codeExist", null, locale));
            return "redirect:/vehicles/new";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        try {
            model.addAttribute("vehicle", vehicleService.getForEdit(id));
            model.addAttribute("listBrands", brandService.listAll());
        } catch (Exception e) {
            model.addAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.edit.error", null, locale));
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
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.update.codeExist", null, locale));
            return "redirect:/vehicles/edit?id=" + vehicleDTO.getId();
        }
    }

    @PostMapping("/delete")
    public String deleteVehicle(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            vehicleService.delete(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.delete.error", null, locale));
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            model.addAttribute("vehicle", vehicleService.getDetail(id));
            return "views/vehicle/vehicle-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.detail.error", null, locale));
            return "redirect:/vehicles";
        }
    }
}