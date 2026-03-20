package org.daw2.tallergo.crud_tallergo.controllers;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal de la aplicación.
 * Gestiona la página de inicio (home) de la web.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final WorkshopService workshopService;

    /**
     * Método que maneja las solicitudes GET a la ruta raíz ("/").
     * Pasa la lista de talleres al modelo para mostrarlos en la home.
     *
     * @param model Modelo de Spring para pasar atributos a la vista.
     * @return Nombre de la vista de inicio ("index").
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("workshops", workshopService.listAll());
        return "index";
    }
}