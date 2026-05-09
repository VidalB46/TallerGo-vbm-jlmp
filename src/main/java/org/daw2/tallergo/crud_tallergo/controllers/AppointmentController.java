package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDTO;
import org.daw2.tallergo.crud_tallergo.dtos.AppointmentDetailDTO;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.services.AppointmentService;
import org.daw2.tallergo.crud_tallergo.services.FileStorageService;
import org.daw2.tallergo.crud_tallergo.services.RepairService;
import org.daw2.tallergo.crud_tallergo.services.VehicleService;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Locale;

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
    private final MessageSource messageSource;

    @GetMapping
    public String listAppointments(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "status") String sort,
                                   Model model,
                                   Authentication auth) {

        User user = userRepository.findByEmailWithRolesAndWorkshop(auth.getName()).orElseThrow();

        if (!"date".equalsIgnoreCase(sort) && !"status".equalsIgnoreCase(sort)) {
            sort = "status";
        }

        Pageable pageable = "date".equalsIgnoreCase(sort)
                ? PageRequest.of(page, 6, Sort.by("startDate").ascending())
                : PageRequest.of(page, 6);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isWorkshopAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WORKSHOP_ADMIN"));

        Page<AppointmentDTO> appointments;

        if ("date".equalsIgnoreCase(sort)) {
            if (isAdmin) {
                appointments = appointmentService.getAllAppointments(pageable);
            } else if (isWorkshopAdmin && user.getWorkshop() != null) {
                appointments = appointmentService.getAppointmentsByWorkshop(user.getWorkshop().getId(), pageable);
            } else {
                appointments = appointmentService.getActiveAppointmentsByUser(user.getId(), pageable);
            }
        } else {
            if (isAdmin) {
                appointments = appointmentService.getAllAppointmentsOrderedByBusinessStatus(pageable);
            } else if (isWorkshopAdmin && user.getWorkshop() != null) {
                appointments = appointmentService.getAppointmentsByWorkshopOrderedByBusinessStatus(user.getWorkshop().getId(), pageable);
            } else {
                appointments = appointmentService.getActiveAppointmentsByUserOrderedByBusinessStatus(user.getId(), pageable);
            }
        }

        model.addAttribute("appointmentsPage", appointments);
        model.addAttribute("sort", sort);
        return "views/appointment/appointment-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        model.addAttribute("appointment", new AppointmentCreateDTO());
        model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
        model.addAttribute("workshops", workshopService.listAll());
        return "views/appointment/appointment-form";
    }

    @PostMapping("/new")
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentCreateDTO dto,
                                    BindingResult result,
                                    Model model,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {

        Locale locale = LocaleContextHolder.getLocale();

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
                    model.addAttribute("error",
                            messageSource.getMessage("msg.appointment.media.invalid", null, locale));
                    model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
                    model.addAttribute("workshops", workshopService.listAll());
                    return "views/appointment/appointment-form";
                }

                String imageWebPath = fileStorageService.saveFile(dto.getMediaFile());
                if (imageWebPath != null) {
                    dto.setMediaUrl(imageWebPath);
                } else {
                    model.addAttribute("error",
                            messageSource.getMessage("msg.appointment.media.save.error", null, locale));
                    model.addAttribute("vehicles", vehicleService.getVehiclesByUserId(user.getId()));
                    model.addAttribute("workshops", workshopService.listAll());
                    return "views/appointment/appointment-form";
                }
            }

            appointmentService.createAppointment(dto, authentication.getName());
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("msg.appointment.create.success", null, locale));
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
        Locale locale = LocaleContextHolder.getLocale();

        try {
            AppointmentDetailDTO appointment = appointmentService.getAppointmentById(id);

            if (Boolean.FALSE.equals(appointment.getIsDateAcceptedByClient())) {
                ra.addFlashAttribute("error",
                        messageSource.getMessage("msg.appointment.confirm.blocked", null, locale));
                return "redirect:/appointments/" + id;
            }

            appointmentService.updateStatus(id, AppointmentStatus.CONFIRMADO);
            repairService.createAutomaticRepair(id);

            ra.addFlashAttribute("success",
                    messageSource.getMessage("msg.appointment.confirm.success", null, locale));

        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    messageSource.getMessage("msg.appointment.confirm.error", null, locale) + e.getMessage());
        }

        return "redirect:/appointments/" + id;
    }

    @PostMapping("/{id}/accept-date")
    public String acceptDate(@PathVariable Long id, RedirectAttributes ra) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            appointmentService.acceptDate(id);
            repairService.createAutomaticRepair(id);

            ra.addFlashAttribute("success",
                    messageSource.getMessage("msg.appointment.accept-date.success", null, locale));

        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    messageSource.getMessage("msg.appointment.accept-date.error", null, locale) + e.getMessage());
        }

        return "redirect:/appointments/" + id;
    }

    @PostMapping("/{id}/reschedule")
    public String reschedule(@PathVariable Long id,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDate,
                             RedirectAttributes ra) {

        Locale locale = LocaleContextHolder.getLocale();

        try {
            appointmentService.updateDate(id, newDate);
            ra.addFlashAttribute("success",
                    messageSource.getMessage("msg.appointment.reschedule.success", null, locale));
        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    messageSource.getMessage("msg.appointment.reschedule.error", null, locale) + e.getMessage());
        }

        return "redirect:/appointments/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            appointmentService.updateStatus(id, AppointmentStatus.CANCELADO);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("msg.appointment.cancel.success", null, locale));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("msg.appointment.cancel.error", null, locale) + e.getMessage());
        }

        return "redirect:/appointments";
    }

    @PostMapping("/{id}/archive")
    public String archive(@PathVariable Long id, RedirectAttributes ra) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            appointmentService.archiveAppointment(id);
            ra.addFlashAttribute("success",
                    messageSource.getMessage("msg.appointment.archive.success", null, locale));
        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    messageSource.getMessage("msg.appointment.archive.error", null, locale));
        }

        return "redirect:/appointments";
    }

    @GetMapping("/history")
    public String showHistory(@RequestParam(defaultValue = "0") int page,
                              Model model,
                              Authentication auth) {

        Pageable pageable = PageRequest.of(page, 9, Sort.by("startDate").descending());

        User user = userRepository.findByEmailWithRolesAndWorkshop(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isWorkshopAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WORKSHOP_ADMIN"));

        Page<AppointmentDTO> historyPage;

        if (isAdmin) {
            historyPage = appointmentService.getAllAppointments(pageable);
        } else if (isWorkshopAdmin && user.getWorkshop() != null) {
            historyPage = appointmentService.getAppointmentsByWorkshop(user.getWorkshop().getId(), pageable);
        } else {
            historyPage = appointmentService.getAppointmentsByUser(user.getId(), pageable);
        }

        model.addAttribute("appointmentsPage", historyPage);
        return "views/appointment/appointment-history";
    }
}