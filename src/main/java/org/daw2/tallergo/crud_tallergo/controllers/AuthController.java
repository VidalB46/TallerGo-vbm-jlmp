package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.UserRegisterDTO;
import org.daw2.tallergo.crud_tallergo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Controlador para el registro de nuevos usuarios.
 * Gestiona el formulario de alta y la validación de contraseñas coincidentes.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * Muestra el formulario de registro de nuevo usuario.
     *
     * @param model Modelo para pasar el DTO vacío a la vista.
     * @return Vista del formulario de registro.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserRegisterDTO());
        return "views/register/register";
    }

    /**
     * Procesa el formulario de registro validando que las contraseñas coincidan.
     *
     * @param userDto  DTO con los datos del nuevo usuario.
     * @param result   Resultado de la validación del formulario.
     * @param model    Modelo para mostrar errores en la vista.
     * @param flash    Atributos flash para el mensaje de éxito tras el registro.
     * @param locale   Configuración regional para mensajes.
     * @return Redirección al login si el registro es exitoso, o recarga del formulario si hay errores.
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") UserRegisterDTO userDto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes flash,
                               Locale locale) {

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.userDto", "Las contraseñas no coinciden");
        }

        if (result.hasErrors()) {
            return "views/register/register";
        }

        try {
            userService.registerNewClient(userDto);

            String successMsg = messageSource.getMessage("msg.register.success",
                    new Object[]{}, "¡Cuenta creada con éxito! Ya puedes iniciar sesión.", locale);

            flash.addFlashAttribute("successMessage", successMsg);
            return "redirect:/login";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "views/register/register";
        }
    }
}