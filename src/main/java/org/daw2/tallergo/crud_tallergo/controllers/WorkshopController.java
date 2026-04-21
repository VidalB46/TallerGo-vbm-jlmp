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

@Controller
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    @GetMapping
    public String list(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<WorkshopDTO> page = workshopService.list(pageable);
        model.addAttribute("page", page);
        return "views/workshop/workshop-list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Integer id, Model model) {
        WorkshopDetailDTO workshop = workshopService.getDetail(id);
        model.addAttribute("workshop", workshop);
        return "views/workshop/workshop-detail";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("workshop", new WorkshopCreateDTO());
        return "views/workshop/workshop-form";
    }

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

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes flash) {
        workshopService.delete(id);
        flash.addFlashAttribute("success", "Taller eliminado correctamente");
        return "redirect:/workshops";
    }

    /**
     * Muestra el formulario de edición usando workshop-edit
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        WorkshopUpdateDTO dto = workshopService.getForEdit(id);
        model.addAttribute("workshop", dto);
        return "views/workshop/workshop-edit";
    }

    /**
     * Guarda los cambios de edición en caso de error usando workshop-edit
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