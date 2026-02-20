package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopDTO;
import org.daw2.tallergo.crud_tallergo.dtos.WorkshopDetailDTO;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    // Listado paginado
    @GetMapping
    public String list(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<WorkshopDTO> page = workshopService.list(pageable);
        model.addAttribute("page", page);
        return "views/workshop/workshop-list"; // Ruta de tu HTML
    }


    @GetMapping("/detail")
    public String detail(@RequestParam("id") Integer id, Model model) {
        WorkshopDetailDTO workshop = workshopService.getDetail(id);
        model.addAttribute("workshop", workshop);
        return "views/workshop/workshop-detail";
    }


    // Mostrar formulario de nuevo taller
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("workshop", new WorkshopCreateDTO());
        return "views/workshop/workshop-form";
    }

    // Insertar nuevo taller
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

    // Eliminar taller
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes flash) {
        workshopService.delete(id);
        flash.addFlashAttribute("success", "Taller eliminado correctamente");
        return "redirect:/workshops";
    }
}