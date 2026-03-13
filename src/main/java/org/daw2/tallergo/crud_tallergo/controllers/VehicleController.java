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

    /**
     * Lista los vehículos con paginación y ordenamiento por modelo.
     *
     * @param pageable Configuración de paginación y ordenamiento.
     * @param model    Modelo para pasar atributos a la vista.
     * @param locale   Configuración regional para mensajes.
     * @return Vista del listado de vehículos ("views/vehicle/vehicle-list").
     */
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

    /**
     * Muestra el formulario para crear un nuevo vehículo.
     * Incluye la lista de marcas disponibles.
     *
     * @param model  Modelo para pasar atributos a la vista.
     * @param locale Configuración regional para mensajes.
     * @return Vista del formulario de vehículo ("views/vehicle/vehicle-form").
     */
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

    /**
     * Inserta un nuevo vehículo en la base de datos.
     * Maneja errores de validación y duplicidad de código.
     *
     * @param vehicleDTO         DTO con los datos del vehículo a crear.
     * @param result             Resultado de la validación del formulario.
     * @param redirectAttributes Atributos flash para mensajes de éxito o error.
     * @param model              Modelo para recargar atributos en caso de error.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección al listado de vehículos o recarga del formulario si hay errores.
     */
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

    /**
     * Muestra el formulario de edición de un vehículo existente.
     *
     * @param id     ID del vehículo a editar.
     * @param model  Modelo para pasar atributos a la vista.
     * @param locale Configuración regional para mensajes.
     * @return Vista del formulario de edición o redirección al listado si ocurre un error.
     */
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

    /**
     * Actualiza un vehículo existente.
     * Maneja errores de validación y duplicidad de código.
     *
     * @param vehicleDTO         DTO con los datos actualizados.
     * @param result             Resultado de la validación.
     * @param redirectAttributes Atributos flash para mensajes.
     * @param model              Modelo para recargar datos en caso de error.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección al listado de vehículos o recarga del formulario si hay errores.
     */
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

    /**
     * Elimina un vehículo por su ID.
     *
     * @param id                 ID del vehículo a eliminar.
     * @param redirectAttributes Atributos flash para mensajes.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección al listado de vehículos.
     */
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

    /**
     * Muestra los detalles de un vehículo.
     *
     * @param id                 ID del vehículo.
     * @param model              Modelo para pasar atributos a la vista.
     * @param redirectAttributes Atributos flash para mensajes.
     * @param locale             Configuración regional para mensajes.
     * @return Vista de detalle del vehículo o redirección al listado si hay error.
     */
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