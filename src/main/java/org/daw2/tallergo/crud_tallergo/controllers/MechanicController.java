package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.services.MechanicService;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la gestión de mecánicos.
 * Permite listar mecánicos, mostrar el formulario de creación y añadir nuevos mecánicos.
 */
@Controller
@RequestMapping("/mechanics")
@RequiredArgsConstructor
public class MechanicController {

    private final MechanicService mechanicService;
    private final WorkshopService workshopService;

    /**
     * Lista los mecánicos existentes con paginación.
     *
     * @param pageable Configuración de la paginación (tamaño de página, orden).
     * @param model    Modelo para pasar atributos a la vista.
     * @return Vista del listado de mecánicos ("views/mechanic/mechanic-list").
     */
    @GetMapping
    public String list(@PageableDefault(size = 10) Pageable pageable, Model model) {
        model.addAttribute("page", mechanicService.list(pageable));
        return "views/mechanic/mechanic-list";
    }

    /**
     * Muestra el formulario para crear un nuevo mecánico.
     * Se incluyen todas las talleres disponibles para seleccionar.
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Vista del formulario de mecánico ("views/mechanic/mechanic-form").
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("mechanic", new MechanicCreateDTO());
        model.addAttribute("listWorkshops", workshopService.listAll());
        return "views/mechanic/mechanic-form";
    }

    /**
     * Inserta un nuevo mecánico en la base de datos.
     * Valida el formulario y, en caso de errores, recarga los talleres disponibles.
     *
     * @param dto    DTO con los datos del mecánico a crear.
     * @param result Resultado de la validación del formulario.
     * @param model  Modelo para pasar atributos a la vista en caso de error.
     * @return Redirección al listado de mecánicos o recarga del formulario si hay errores.
     */
    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("mechanic") MechanicCreateDTO dto,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            // Recargar lista de talleres si hay errores para volver a mostrar el formulario correctamente
            model.addAttribute("listWorkshops", workshopService.listAll());
            return "views/mechanic/mechanic-form";
        }
        mechanicService.create(dto);
        return "redirect:/mechanics";
    }
}