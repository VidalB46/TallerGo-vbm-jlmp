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

/**
 * Controlador para la gestión de la recuperación de contraseñas.
 * Permite solicitar un restablecimiento de contraseña y establecer una nueva contraseña mediante un token.
 */
@Controller
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Muestra el formulario para solicitar el restablecimiento de contraseña.
     *
     * @param model Modelo de Spring para pasar atributos a la vista.
     * @return Vista del formulario de "olvidé mi contraseña".
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("dto", new PasswordResetRequestDTO());
        return "views/reset-password/forgot-password";
    }

    /**
     * Gestiona la solicitud de restablecimiento de contraseña.
     * Envía un correo con un token si el email existe en el sistema.
     *
     * @param dto                 DTO con el email para solicitar el restablecimiento.
     * @param result              Resultado de la validación del formulario.
     * @param request             HttpServletRequest para obtener información del cliente (IP y User-Agent).
     * @param redirectAttributes  Atributos flash para enviar mensajes a la vista.
     * @return Redirección al mismo formulario con mensaje de éxito o recarga si hay errores de validación.
     */
    @PostMapping("/forgot")
    public String handleForgotPassword(
            @Valid @ModelAttribute("dto") PasswordResetRequestDTO dto,
            BindingResult result,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "views/reset-password/forgot-password";
        }

        // Obtener IP y User-Agent para auditoría o logging
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        passwordResetService.requestPasswordReset(dto.getEmail(), ip, userAgent);

        Locale locale = LocaleContextHolder.getLocale();
        String msg = messageSource.getMessage("msg.password-reset.request.sent", null, locale);
        redirectAttributes.addFlashAttribute("successMessage", msg);

        return "redirect:/auth/forgot-password";
    }

    /**
     * Muestra el formulario para establecer una nueva contraseña utilizando un token.
     *
     * @param token Token único enviado al correo del usuario.
     * @param model Modelo de Spring para pasar atributos a la vista.
     * @return Vista del formulario de restablecimiento de contraseña.
     */
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        PasswordResetDTO dto = new PasswordResetDTO();
        dto.setToken(token);
        model.addAttribute("dto", dto);
        return "views/reset-password/reset-password";
    }

    /**
     * Gestiona la acción de restablecer la contraseña.
     * Valida que las contraseñas coincidan y que el token sea válido.
     *
     * @param dto                DTO con la nueva contraseña y el token.
     * @param result             Resultado de la validación del formulario.
     * @param redirectAttributes Atributos flash para enviar mensajes a la vista.
     * @return Redirección al login si tiene éxito, o al formulario de "olvidé mi contraseña" si hay error.
     */
    @PostMapping("/reset-password")
    public String handleResetPassword(
            @Valid @ModelAttribute("dto") PasswordResetDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Validar que la nueva contraseña y su confirmación coincidan
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