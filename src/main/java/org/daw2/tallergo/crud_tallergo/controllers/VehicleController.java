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
     * Lista los vehículos con paginación y búsqueda.
     * El administrador ve todos; el cliente solo ve los suyos.
     *
     * @param pageable       Configuración de página y ordenamiento.
     * @param q              Texto de búsqueda (modelo, matrícula o VIN).
     * @param model          Modelo para pasar atributos a la vista.
     * @param locale         Configuración regional para mensajes.
     * @param authentication Información del usuario autenticado (rol y email).
     * @return Vista del listado de vehículos.
     */
    @GetMapping
    public String listVehicles(
            @PageableDefault(size = 8, sort = "model", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "q", required = false, defaultValue = "") String q,
            Model model,
            Locale locale,
            Authentication authentication) {

        try {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            Page<VehicleDTO> page;
            if (q.isBlank()) {
                page = isAdmin ? vehicleService.list(pageable) : vehicleService.listByUser(user.getId(), pageable);
            } else {
                page = isAdmin ? vehicleService.search(q.trim(), pageable) : vehicleService.searchByUser(q.trim(), user.getId(), pageable);
            }

            model.addAttribute("page", page);
            model.addAttribute("q", q);

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

    /**
     * Muestra el formulario de alta de un nuevo vehículo con las marcas disponibles.
     *
     * @param model  Modelo para pasar el DTO vacío y la lista de marcas.
     * @param locale Configuración regional para mensajes de error.
     * @return Vista del formulario de creación de vehículo.
     */
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

    /**
     * Procesa el formulario de creación y registra el vehículo a nombre del usuario autenticado.
     *
     * @param vehicleDTO         DTO con los datos del vehículo a crear.
     * @param result             Resultado de la validación del formulario.
     * @param redirectAttributes Atributos flash para mensajes de error.
     * @param model              Modelo para recargar la lista de marcas en caso de error.
     * @param locale             Configuración regional para mensajes.
     * @param authentication     Información del usuario autenticado.
     * @return Redirección al listado de vehículos o recarga del formulario si hay errores.
     */
    @PostMapping("/insert")
    public String insertVehicle(@Valid @ModelAttribute("vehicle") VehicleCreateDTO vehicleDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model,
                                Locale locale,
                                Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("listBrands", brandService.listAll());
            return "views/vehicle/vehicle-form";
        }
        try {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            vehicleService.create(vehicleDTO, user.getId());
            return "redirect:/vehicles";
        } catch (DuplicateResourceException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.insert.codeExist", null, locale));
            return "redirect:/vehicles/new";
        }
    }

    /**
     * Muestra el formulario de edición de un vehículo existente.
     *
     * @param id     ID del vehículo a editar.
     * @param model  Modelo para pasar el DTO y la lista de marcas.
     * @param locale Configuración regional para mensajes de error.
     * @return Vista del formulario de edición o redirección al listado si hay error.
     */
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

    /**
     * Procesa el formulario de edición y actualiza los datos del vehículo.
     *
     * @param vehicleDTO         DTO con los datos actualizados del vehículo.
     * @param result             Resultado de la validación del formulario.
     * @param redirectAttributes Atributos flash para mensajes de error.
     * @param model              Modelo para recargar la lista de marcas en caso de error.
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
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.update.codeExist", null, locale));
            return "redirect:/vehicles/edit?id=" + vehicleDTO.getId();
        }
    }

    /**
     * Elimina un vehículo por su ID.
     *
     * @param id                 ID del vehículo a eliminar.
     * @param redirectAttributes Atributos flash para mensajes de error.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección al listado de vehículos.
     */
    @PostMapping("/delete")
    public String deleteVehicle(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            vehicleService.delete(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("msg.vehicle-controller.delete.error", null, locale));
        }
        return "redirect:/vehicles";
    }

    /**
     * Muestra el detalle completo de un vehículo con su marca.
     *
     * @param id                 ID del vehículo a mostrar.
     * @param model              Modelo para pasar el DTO a la vista.
     * @param redirectAttributes Atributos flash para mensajes de error.
     * @param locale             Configuración regional para mensajes.
     * @return Vista de detalle del vehículo o redirección al listado si hay error.
     */
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