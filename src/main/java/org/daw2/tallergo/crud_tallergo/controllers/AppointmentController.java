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

/**
 * Controlador para la gestión del ciclo de vida de las citas y la orquestación con reparaciones.
 */
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

    /**
     * Lista las citas del usuario actual o todas si es administrador.
     */
    @GetMapping
    public String listAppointments(@RequestParam(defaultValue = "0") int page, Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        Pageable pageable = PageRequest.of(page, 10, Sort.by("startDate").descending());

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Page<AppointmentDTO> appointments = isAdmin ? appointmentService.getAllAppointments(pageable) : appointmentService.getAppointmentsByUser(user.getId(), pageable);

        model.addAttribute("appointmentsPage", appointments);
        return "views/appointment/appointment-list";
    }

    /**
     * Muestra el formulario para solicitar una nueva cita.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        model.addAttribute("appointment", new AppointmentCreateDTO());
        model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
        model.addAttribute("workshops", workshopService.listAll());

        return "views/appointment/appointment-form";
    }

    /**
     * Procesa y valida el formulario de nueva cita.
     */
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

    /**
     * Visualiza los detalles completos de una cita.
     */
    @GetMapping("/{id}")
    public String viewAppointment(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", appointmentService.getAppointmentById(id));
        return "views/appointment/appointment-detail";
    }

    /**
     * Procesa la confirmación manual de una cita por parte del administrador.
     */
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

    /**
     * Gestiona la aceptación de una propuesta de fecha por parte del cliente.
     */
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

    /**
     * Registra una nueva propuesta de fecha y hora para la cita.
     */
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

    /**
     * Cancela la cita de forma definitiva (Taller o Cliente).
     */
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
}