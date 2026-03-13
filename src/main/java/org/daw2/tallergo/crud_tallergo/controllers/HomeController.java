package org.daw2.tallergo.crud_tallergo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal de la aplicación.
 * Gestiona la página de inicio (home) de la web.
 */
@Controller
public class HomeController {

    /**
     * Método que maneja las solicitudes GET a la ruta raíz ("/").
     *
     * @param model Modelo de Spring para pasar atributos a la vista (no usado actualmente).
     * @return Nombre de la vista de inicio ("index").
     */
    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }
}