package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.dtos.BrandCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandUpdateDTO;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.services.BrandService;
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

import java.util.Locale;

/**
 * Controlador para la gestión de marcas en la aplicación.
 * Permite listar, crear, actualizar, eliminar y ver detalles de las marcas.
 */
@Controller
@RequestMapping("/brands")
public class BrandController {

    private static final Logger logger = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BrandService brandService;

    /**
     * Lista todas las marcas con paginación.
     *
     * @param pageable Configuración de la paginación y ordenamiento.
     * @param model    Modelo para pasar atributos a la vista.
     * @return Nombre de la vista de listado de marcas.
     */
    @GetMapping
    public String listBrands(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        try {
            Page<BrandDTO> page = brandService.list(pageable);
            model.addAttribute("page", page);

            String sortParam = "name,asc";
            if (page.getSort().isSorted()) {
                Sort.Order order = page.getSort().iterator().next();
                sortParam = order.getProperty() + "," + order.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al listar las marcas.");
        }

        return "views/brand/brand-list";
    }

    /**
     * Muestra el formulario para crear una nueva marca.
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Vista del formulario de marca.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("brand", new BrandCreateDTO());
        return "views/brand/brand-form";
    }

    /**
     * Inserta una nueva marca en la base de datos.
     *
     * @param brandDTO           DTO con los datos de la marca a crear.
     * @param result             Resultado de la validación del formulario.
     * @param redirectAttributes Atributos para redirigir mensajes flash.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección a la lista de marcas o al formulario en caso de error.
     */
    @PostMapping("/insert")
    public String insertBrand(@Valid @ModelAttribute("brand") BrandCreateDTO brandDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Locale locale) {

        if (result.hasErrors()) {
            return "views/brand/brand-form";
        }

        try {
            brandService.create(brandDTO);
            return "redirect:/brands";

        } catch (DuplicateResourceException ex) {
            String errorMessage = messageSource.getMessage("msg.brand-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/brands/new";
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.brand-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/brands/new";
        }
    }

    /**
     * Muestra el formulario para editar una marca existente.
     *
     * @param id                 ID de la marca a editar.
     * @param model              Modelo para pasar atributos a la vista.
     * @param redirectAttributes Atributos para redirigir mensajes flash.
     * @param locale             Configuración regional para mensajes.
     * @return Vista del formulario de edición o redirección si no se encuentra la marca.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Integer id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            BrandUpdateDTO brandDTO = brandService.getForEdit(id);
            model.addAttribute("brand", brandDTO);
            return "views/brand/brand-form";
        } catch (ResourceNotFoundException ex) {
            String msg = messageSource.getMessage("msg.brand-controller.detail.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/brands";
        }
    }

    /**
     * Actualiza una marca existente.
     *
     * @param brandDTO           DTO con los datos actualizados de la marca.
     * @param result             Resultado de la validación del formulario.
     * @param redirectAttributes Atributos para redirigir mensajes flash.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección a la lista de marcas o al formulario en caso de error.
     */
    @PostMapping("/update")
    public String updateBrand(@Valid @ModelAttribute("brand") BrandUpdateDTO brandDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Locale locale) {

        if (result.hasErrors()) {
            return "views/brand/brand-form";
        }

        try {
            brandService.update(brandDTO);
            return "redirect:/brands";

        } catch (DuplicateResourceException ex) {
            String errorMessage = messageSource.getMessage("msg.brand-controller.update.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/brands/edit?id=" + brandDTO.getId();
        }
    }

    /**
     * Elimina una marca por su ID.
     *
     * @param id                 ID de la marca a eliminar.
     * @param redirectAttributes Atributos para redirigir mensajes flash.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección a la lista de marcas.
     */
    @PostMapping("/delete")
    public String deleteBrand(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            brandService.delete(id);
            return "redirect:/brands";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la marca.");
            return "redirect:/brands";
        }
    }

    /**
     * Muestra los detalles de una marca.
     *
     * @param id                 ID de la marca.
     * @param model              Modelo para pasar atributos a la vista.
     * @param redirectAttributes Atributos para redirigir mensajes flash.
     * @param locale             Configuración regional para mensajes.
     * @return Vista con el detalle de la marca o redirección si ocurre un error.
     */
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Integer id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            BrandDetailDTO brandDTO = brandService.getDetail(id);
            model.addAttribute("brand", brandDTO);
            return "views/brand/brand-detail";
        } catch (Exception e) {
            String msg = messageSource.getMessage("msg.brand-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/brands";
        }
    }
}