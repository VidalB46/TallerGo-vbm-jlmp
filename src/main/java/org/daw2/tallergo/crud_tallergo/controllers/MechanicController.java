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

@Controller
@RequestMapping("/mechanics")
@RequiredArgsConstructor
public class MechanicController {

    private final MechanicService mechanicService;
    private final WorkshopService workshopService;

    @GetMapping
    public String list(@PageableDefault(size = 10) Pageable pageable, Model model) {
        model.addAttribute("page", mechanicService.list(pageable));
        return "views/mechanic/mechanic-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("mechanic", new MechanicCreateDTO());
        model.addAttribute("listWorkshops", workshopService.listAll());
        return "views/mechanic/mechanic-form";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("mechanic") MechanicCreateDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listWorkshops", workshopService.listAll());
            return "views/mechanic/mechanic-form";
        }
        mechanicService.create(dto);
        return "redirect:/mechanics";
    }
}