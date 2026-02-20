package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.dtos.PasswordResetDTO;
import org.daw2.tallergo.crud_tallergo.dtos.PasswordResetRequestDTO;
import org.daw2.tallergo.crud_tallergo.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("dto", new PasswordResetRequestDTO());
        return "views/reset-password/forgot-password";
    }

    @PostMapping("/forgot")
    public String handleForgotPassword(
            @Valid @ModelAttribute("dto") PasswordResetRequestDTO dto,
            BindingResult result,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "views/reset-password/forgot-password";
        }

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        passwordResetService.requestPasswordReset(dto.getEmail(), ip, userAgent);

        Locale locale = LocaleContextHolder.getLocale();
        String msg = messageSource.getMessage("msg.password-reset.request.sent", null, locale);
        redirectAttributes.addFlashAttribute("successMessage", msg);

        return "redirect:/auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setToken(token);
        model.addAttribute("dto", dto);
        return "views/reset-password/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @Valid @ModelAttribute("dto") PasswordResetDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (dto.getNewPassword() != null && dto.getConfirmPassword() != null
                && !dto.getNewPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "password.mismatch");
        }

        if (result.hasErrors()) {
            return "views/reset-password/reset-password";
        }

        Locale locale = LocaleContextHolder.getLocale();

        try {
            passwordResetService.resetPassword(dto.getToken(), dto.getNewPassword());
            String msg = messageSource.getMessage("msg.password-reset.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", msg);
            return "redirect:/login";

        } catch (IllegalArgumentException ex) {
            String msg = messageSource.getMessage("msg.password-reset.invalid", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/auth/forgot-password";
        }
    }
}