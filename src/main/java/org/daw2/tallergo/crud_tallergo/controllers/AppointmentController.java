package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.services.AppointmentService;
import org.daw2.tallergo.crud_tallergo.services.VehicleService;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final VehicleService vehicleService;
    private final WorkshopService workshopService;
    private final UserRepository userRepository;

    @GetMapping
    public String listAppointments(@RequestParam(defaultValue = "0") int page,
                                   Model model,
                                   Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("startDate").descending());
        Page<AppointmentDTO> appointments;

        if (isAdmin) {
            appointments = appointmentService.getAllAppointments(pageable);
        } else {
            appointments = appointmentService.getAppointmentsByUser(user.getId(), pageable);
        }

        model.addAttribute("appointmentsPage", appointments);
        // RUTA ACTUALIZADA A TU ESTRUCTURA
        return "views/appointment/appointment-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        model.addAttribute("appointment", new AppointmentCreateDTO());
        model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
        model.addAttribute("workshops", workshopService.listAll());

        // RUTA ACTUALIZADA A TU ESTRUCTURA
        return "views/appointment/appointment-form";
    }

    @PostMapping("/new")
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentCreateDTO dto,
                                    BindingResult result,
                                    Model model,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (result.hasErrors()) {
            model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
            model.addAttribute("workshops", workshopService.listAll());
            // RUTA ACTUALIZADA A TU ESTRUCTURA
            return "views/appointment/appointment-form";
        }

        try {
            appointmentService.createAppointment(dto, authentication.getName());
            redirectAttributes.addFlashAttribute("success", "¡Tu cita ha sido solicitada correctamente!");
            // El redirect se queda igual porque apunta a la URL del navegador, no a la carpeta física
            return "redirect:/appointments";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
            model.addAttribute("workshops", workshopService.listAll());
            return "views/appointment/appointment-form";
        }
    }

    @GetMapping("/{id}")
    public String viewAppointment(@PathVariable Long id, Model model) {
        AppointmentDetailDTO detail = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment", detail);
        return "views/appointment/appointment-detail";
    }
}