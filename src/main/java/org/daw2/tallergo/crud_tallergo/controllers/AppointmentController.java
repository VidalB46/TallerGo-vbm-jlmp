package org.daw2.tallergo.crud_tallergo.controllers;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.services.*;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final VehicleService vehicleService;
    private final WorkshopService workshopService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final RepairService repairService;

    @GetMapping
    public String listAppointments(@RequestParam(defaultValue = "0") int page, Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        Pageable pageable = PageRequest.of(page, 10, Sort.by("startDate").ascending());

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        // El servicio getAllAppointments devolverá las activas para el cliente
        Page<AppointmentDTO> appointments = isAdmin ? appointmentService.getAllAppointments(pageable) : appointmentService.getActiveAppointmentsByUser(user.getId(), pageable);

        model.addAttribute("appointmentsPage", appointments);
        return "views/appointment/appointment-list";
    }

    // --- RUTAS DE CREACIÓN Y EDICIÓN  ---
    @GetMapping("/new")
    public String showCreateForm(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("appointment", new AppointmentCreateDTO());
        model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
        model.addAttribute("workshops", workshopService.listAll());
        return "views/appointment/appointment-form";
    }

    @PostMapping("/new")
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentCreateDTO dto, BindingResult result, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (result.hasErrors()) {
            model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
            model.addAttribute("workshops", workshopService.listAll());
            return "views/appointment/appointment-form";
        }

        try {
            if (dto.getMediaFile() != null && !dto.getMediaFile().isEmpty()) {
                String contentType = dto.getMediaFile().getContentType();
                if (contentType == null || (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
                    model.addAttribute("error", "El archivo adjunto debe ser una imagen o un vídeo válido.");
                    model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
                    model.addAttribute("workshops", workshopService.listAll());
                    return "views/appointment/appointment-form";
                }

                String imageWebPath = fileStorageService.saveFile(dto.getMediaFile());
                if (imageWebPath != null) {
                    dto.setMediaUrl(imageWebPath);
                } else {
                    model.addAttribute("error", "No se pudo guardar el archivo multimedia. Inténtalo de nuevo.");
                    model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
                    model.addAttribute("workshops", workshopService.listAll());
                    return "views/appointment/appointment-form";
                }
            }
            appointmentService.createAppointment(dto, authentication.getName());
            redirectAttributes.addFlashAttribute("success", "¡Tu cita ha sido solicitada correctamente!");
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
        model.addAttribute("appointment", appointmentService.getAppointmentById(id));
        return "views/appointment/appointment-detail";
    }

    @PostMapping("/{id}/confirm")
    public String confirmAppointment(@PathVariable Long id, RedirectAttributes ra) {
        try {
            appointmentService.updateStatus(id, org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus.CONFIRMADO);
            repairService.createAutomaticRepair(id);
            ra.addFlashAttribute("success", "Cita confirmada. Se ha abierto el expediente de reparación.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al confirmar: " + e.getMessage());
        }
        return "redirect:/appointments/" + id;
    }

    @PostMapping("/{id}/accept-date")
    public String acceptDate(@PathVariable Long id, RedirectAttributes ra) {
        try {
            appointmentService.acceptDate(id);
            repairService.createAutomaticRepair(id);
            ra.addFlashAttribute("success", "Nueva fecha aceptada. La cita ha sido confirmada.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al procesar aceptación: " + e.getMessage());
        }
        return "redirect:/appointments/" + id;
    }

    @PostMapping("/{id}/reschedule")
    public String reschedule(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDate, RedirectAttributes ra) {
        try {
            appointmentService.updateDate(id, newDate);
            ra.addFlashAttribute("success", "Propuesta de cambio de fecha enviada al cliente.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al reprogramar: " + e.getMessage());
        }
        return "redirect:/appointments/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.updateStatus(id, org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus.CANCELADO);
            redirectAttributes.addFlashAttribute("success", "Cita cancelada/rechazada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar: " + e.getMessage());
        }
        return "redirect:/appointments";
    }

    // --- RUTAS PARA EL HISTORIAL Y EL ARCHIVADO ---

    @PostMapping("/{id}/archive")
    public String archive(@PathVariable Long id, RedirectAttributes ra) {
        try {
            appointmentService.archiveAppointment(id);
            ra.addFlashAttribute("success", "Cita ocultada de tu vista principal. Puedes consultarla en el Historial.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al ocultar la cita.");
        }
        return "redirect:/appointments";
    }

    @GetMapping("/history")
    public String showHistory(@RequestParam(defaultValue = "0") int page, Model model, Authentication auth) {
        // Ordenamos por fecha descendente para ver lo más reciente primero
        Pageable pageable = PageRequest.of(page, 9, Sort.by("startDate").descending());

        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        // Llamamos al servicio que ejecuta findFullHistoryByUserId
        Page<AppointmentDTO> historyPage = appointmentService.getAppointmentsByUser(user.getId(), pageable);

        model.addAttribute("appointmentsPage", historyPage);
        return "views/appointment/appointment-history";
    }
}