package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.dtos.UserProfileFormDTO;
import org.daw2.tallergo.crud_tallergo.exceptions.InvalidFileException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.services.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

/**
 * Controlador para la gestión del perfil de usuario.
 * Permite mostrar, actualizar la información personal y subir la imagen de perfil.
 */
@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserProfileService userProfileService;

    /**
     * Muestra el formulario de edición del perfil del usuario autenticado.
     *
     * @param model     Modelo para pasar atributos a la vista.
     * @param locale    Configuración regional para los mensajes.
     * @param principal Información del usuario autenticado.
     * @return Vista del formulario de perfil ("views/user-profile/user-profile-form").
     */
    @GetMapping("/edit")
    public String showProfileForm(Model model, Locale locale, Principal principal) {
        String email = principal.getName();
        logger.info("Mostrando formulario de perfil para {}", email);

        try {
            UserProfileFormDTO formDto = userProfileService.getFormByEmail(email);
            model.addAttribute("userProfileForm", formDto);
            return "views/user-profile/user-profile-form";

        } catch (ResourceNotFoundException ex) {
            String errorMessage = messageSource.getMessage("msg.user-controller.edit.notfound", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            return "views/user-profile/user-profile-form";

        } catch (Exception ex) {
            String errorMessage = messageSource.getMessage("msg.userProfile.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            return "views/user-profile/user-profile-form";
        }
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     * Permite cambiar datos personales y subir una imagen de perfil.
     *
     * @param profileDto        DTO con los datos actualizados del usuario.
     * @param result            Resultado de la validación del formulario.
     * @param profileImageFile  Imagen de perfil subida (opcional).
     * @param redirectAttributes Atributos flash para mensajes de éxito o error.
     * @param locale            Configuración regional para mensajes.
     * @param principal         Información del usuario autenticado.
     * @return Redirección al formulario de edición del perfil.
     */
    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("userProfileForm") UserProfileFormDTO profileDto,
                                BindingResult result,
                                @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
                                RedirectAttributes redirectAttributes,
                                Locale locale,
                                Principal principal) {
        String email = principal.getName();

        if (result.hasErrors()) {
            return "views/user-profile/user-profile-form";
        }

        try {
            userProfileService.updateProfile(email, profileDto, profileImageFile);
            String successMessage = messageSource.getMessage("msg.userProfile.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (ResourceNotFoundException ex) {
            String errorMessage = messageSource.getMessage("msg.user-controller.edit.notfound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

        } catch (InvalidFileException ex) {
            String errorMessage = messageSource.getMessage("msg.userProfile.image.invalid", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

        } catch (Exception ex) {
            String errorMessage = messageSource.getMessage("msg.userProfile.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/profile/edit";
    }
}