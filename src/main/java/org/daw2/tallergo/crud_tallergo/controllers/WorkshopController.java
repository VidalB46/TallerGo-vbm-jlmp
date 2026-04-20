package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopDTO;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopUpdateDTO;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión de talleres.
 * Permite listar, crear, eliminar y mostrar detalles de talleres.
 */
@Controller
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    /**
     * Lista los talleres con paginación.
     *
     * @param pageable Configuración de paginación.
     * @param model    Modelo para pasar atributos a la vista.
     * @return Vista de listado de talleres ("views/workshop/workshop-list").
     */
    @GetMapping
    public String list(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<WorkshopDTO> page = workshopService.list(pageable);
        model.addAttribute("page", page);
        return "views/workshop/workshop-list";
    }

    /**
     * Muestra los detalles de un taller por su ID.
     *
     * @param id    ID del taller.
     * @param model Modelo para pasar atributos a la vista.
     * @return Vista de detalle del taller ("views/workshop/workshop-detail").
     */
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Integer id, Model model) {
        WorkshopDetailDTO workshop = workshopService.getDetail(id);
        model.addAttribute("workshop", workshop);
        return "views/workshop/workshop-detail";
    }

    /**
     * Muestra el formulario para crear un nuevo taller.
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Vista del formulario de taller ("views/workshop/workshop-form").
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("workshop", new WorkshopCreateDTO());
        return "views/workshop/workshop-form";
    }

    /**
     * Inserta un nuevo taller en la base de datos.
     *
     * @param dto   DTO con los datos del taller.
     * @param result Resultado de la validación del formulario.
     * @param flash  Atributos flash para mensajes de éxito o error.
     * @return Redirección al listado de talleres o recarga del formulario si hay errores.
     */
    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("workshop") WorkshopCreateDTO dto,
                         BindingResult result,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "views/workshop/workshop-form";
        }

        try {
            workshopService.create(dto);
            flash.addFlashAttribute("success", "Taller creado correctamente");
            return "redirect:/workshops";
        } catch (Exception e) {
            result.rejectValue("nif", "error.workshop", e.getMessage());
            return "views/workshop/workshop-form";
        }
    }

    /**
     * Elimina un taller por su ID.
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes flash) {
        workshopService.delete(id);
        flash.addFlashAttribute("success", "Taller eliminado correctamente");
        return "redirect:/workshops";
    }

    /**
     * Muestra el formulario de edición de un taller existente.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        WorkshopUpdateDTO dto = workshopService.getForEdit(id);
        model.addAttribute("workshop", dto);
        model.addAttribute("editMode", true);
        return "views/workshop/workshop-edit";
    }

    /**
     * Guarda los cambios de edición de un taller.
     */
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("workshop") WorkshopUpdateDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("editMode", true);
            return "views/workshop/workshop-edit";
        }
        try {
            workshopService.update(dto);
            flash.addFlashAttribute("successMessage", "Taller actualizado correctamente");
            return "redirect:/workshops/detail?id=" + dto.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("editMode", true);
            return "views/workshop/workshop-edit";
        }
    }
}