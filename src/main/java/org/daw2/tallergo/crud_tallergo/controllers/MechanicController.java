package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDetailDTO;
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
     * Lista los mecánicos con paginación y búsqueda opcional.
     *
     * @param pageable Configuración de página y tamaño (por defecto 8 elementos).
     * @param q        Texto de búsqueda por nombre o especialidad (opcional).
     * @param model    Modelo para pasar atributos a la vista.
     * @return Vista del listado de mecánicos.
     */
    @GetMapping
    public String list(@PageableDefault(size = 8) Pageable pageable,
                       @RequestParam(value = "q", required = false, defaultValue = "") String q,
                       Model model) {
        model.addAttribute("page", q.isBlank()
                ? mechanicService.list(pageable)
                : mechanicService.search(q.trim(), pageable));
        model.addAttribute("q", q);
        return "views/mechanic/mechanic-list";
    }

    /**
     * Muestra el detalle completo de un mecánico con su taller asociado.
     *
     * @param id    ID del mecánico a mostrar.
     * @param model Modelo para pasar el DTO de detalle a la vista.
     * @return Vista de detalle del mecánico.
     */
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model) {
        model.addAttribute("mechanic", mechanicService.getDetail(id));
        return "views/mechanic/mechanic-detail";
    }

    /**
     * Muestra el formulario para dar de alta un nuevo mecánico con la lista de talleres disponibles.
     *
     * @param model Modelo para pasar el DTO vacío y la lista de talleres.
     * @return Vista del formulario de creación de mecánico.
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