package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.services.MechanicService;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
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
    private final UserRepository userRepository;

    @GetMapping
    public String list(@PageableDefault(size = 8) Pageable pageable,
                       @RequestParam(value = "q", required = false, defaultValue = "") String q,
                       Model model,
                       Authentication authentication) {

        User user = userRepository.findByEmailWithRolesAndWorkshop(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isWorkshopAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WORKSHOP_ADMIN"));

        Page<MechanicDTO> page;

        if (isWorkshopAdmin && !isAdmin) {
            if (user.getWorkshop() == null) {
                page = Page.empty(pageable);
            } else {
                page = q.isBlank()
                        ? mechanicService.listByWorkshop(user.getWorkshop().getId(), pageable)
                        : mechanicService.searchByWorkshop(user.getWorkshop().getId(), q.trim(), pageable);
            }
        } else {
            page = q.isBlank()
                    ? mechanicService.list(pageable)
                    : mechanicService.search(q.trim(), pageable);
        }

        model.addAttribute("page", page);
        model.addAttribute("q", q);
        return "views/mechanic/mechanic-list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") Long id, Model model) {
        model.addAttribute("mechanic", mechanicService.getDetail(id));
        return "views/mechanic/mechanic-detail";
    }

    @GetMapping("/new")
    public String showForm(Model model, Authentication authentication) {
        User user = userRepository.findByEmailWithRolesAndWorkshop(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isWorkshopAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WORKSHOP_ADMIN"));

        MechanicCreateDTO dto = new MechanicCreateDTO();

        if (isWorkshopAdmin && !isAdmin) {
            if (user.getWorkshop() == null) {
                throw new IllegalArgumentException("Este usuario no tiene taller asignado");
            }
            dto.setWorkshopId(user.getWorkshop().getId());
            model.addAttribute("assignedWorkshopName", user.getWorkshop().getName());
        } else {
            model.addAttribute("listWorkshops", workshopService.listAll());
        }

        model.addAttribute("mechanic", dto);
        return "views/mechanic/mechanic-form";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("mechanic") MechanicCreateDTO dto,
                         BindingResult result,
                         Model model,
                         Authentication authentication) {

        User user = userRepository.findByEmailWithRolesAndWorkshop(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isWorkshopAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WORKSHOP_ADMIN"));

        if (isWorkshopAdmin && !isAdmin) {
            if (user.getWorkshop() == null) {
                result.reject("mechanic.workshop", "El usuario de taller no tiene un taller asignado.");
            } else {
                dto.setWorkshopId(user.getWorkshop().getId());
                model.addAttribute("assignedWorkshopName", user.getWorkshop().getName());
            }
        } else {
            model.addAttribute("listWorkshops", workshopService.listAll());
        }

        if (result.hasErrors()) {
            return "views/mechanic/mechanic-form";
        }

        mechanicService.create(dto);
        return "redirect:/mechanics";
    }
}