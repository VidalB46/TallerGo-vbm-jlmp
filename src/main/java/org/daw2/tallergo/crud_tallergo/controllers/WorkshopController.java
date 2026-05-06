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
 * Controlador para la gestión de talleres mecánicos.
 * Permite listar, buscar, crear, editar, actualizar, eliminar y ver el detalle de talleres.
 */
@Controller
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    /**
     * Lista los talleres con paginación y búsqueda opcional por nombre.
     *
     * @param pageable Configuración de página y tamaño (por defecto 8 elementos).
     * @param q        Texto de búsqueda por nombre (opcional).
     * @param model    Modelo para pasar atributos a la vista.
     * @return Vista del listado de talleres.
     */
    @GetMapping
    public String list(@PageableDefault(size = 8) Pageable pageable,
                       @RequestParam(value = "q", required = false, defaultValue = "") String q,
                       Model model) {
        Page<WorkshopDTO> page = q.isBlank()
                ? workshopService.list(pageable)
                : workshopService.search(q.trim(), pageable);
        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "views/workshop/workshop-list";
    }

    /**
     * Muestra el detalle completo de un taller, incluyendo su lista de mecánicos.
     *
     * @param id    ID del taller a mostrar.
     * @param model Modelo para pasar atributos a la vista.
     * @return Vista de detalle del taller.
     */
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Integer id, Model model) {
        WorkshopDetailDTO workshop = workshopService.getDetail(id);
        model.addAttribute("workshop", workshop);
        return "views/workshop/workshop-detail";
    }

    /**
     * Muestra el formulario de alta de un nuevo taller.
     *
     * @param model Modelo para pasar el DTO vacío a la vista.
     * @return Vista del formulario de creación.
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("workshop", new WorkshopCreateDTO());
        return "views/workshop/workshop-form";
    }

    /**
     * Procesa el formulario de creación de un nuevo taller.
     *
     * @param dto    DTO con los datos del taller a crear.
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
     *
     * @param id    ID del taller a eliminar.
     * @param flash Atributos flash para mensaje de confirmación.
     * @return Redirección al listado de talleres.
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes flash) {
        workshopService.delete(id);
        flash.addFlashAttribute("success", "Taller eliminado correctamente");
        return "redirect:/workshops";
    }

    /**
     * Muestra el formulario de edición de un taller existente.
     *
     * @param id    ID del taller a editar.
     * @param model Modelo para pasar el DTO con los datos actuales a la vista.
     * @return Vista del formulario de edición.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        WorkshopUpdateDTO dto = workshopService.getForEdit(id);
        model.addAttribute("workshop", dto);
        return "views/workshop/workshop-edit";
    }

    /**
     * Procesa el formulario de edición y guarda los cambios del taller.
     *
     * @param dto    DTO con los datos actualizados del taller.
     * @param result Resultado de la validación del formulario.
     * @param model  Modelo para pasar atributos a la vista en caso de error.
     * @param flash  Atributos flash para mensaje de éxito.
     * @return Redirección al detalle del taller o recarga del formulario si hay errores.
     */
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("workshop") WorkshopUpdateDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "views/workshop/workshop-edit";
        }
        try {
            workshopService.update(dto);
            flash.addFlashAttribute("successMessage", "Taller actualizado correctamente");
            return "redirect:/workshops/detail?id=" + dto.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "views/workshop/workshop-edit";
        }
    }
}