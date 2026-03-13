package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para gestionar el acceso a la página de login.
 * Muestra el formulario de inicio de sesión y mensajes de error si existen.
 */
@Controller
public class LoginController {

    /**
     * Maneja las solicitudes GET a la ruta "/login".
     * Recupera cualquier mensaje de error de sesión y lo pasa a la vista.
     *
     * @param request HttpServletRequest para acceder a la sesión y sus atributos.
     * @param model   Modelo de Spring para pasar atributos a la vista.
     * @return Nombre de la vista del login ("views/login/login").
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        // Recupera el mensaje de error de la sesión si existe
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            // Elimina el mensaje de error de la sesión para que no persista
            request.getSession().removeAttribute("errorMessage");
        }
        return "views/login/login";
    }
}