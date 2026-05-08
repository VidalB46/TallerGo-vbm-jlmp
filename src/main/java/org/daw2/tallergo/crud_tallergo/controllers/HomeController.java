package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.services.WorkshopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        var all = workshopService.listAll();
        model.addAttribute("workshops", all.subList(0, Math.min(4, all.size())));
        return "index";
    }

    /**
     * Cambia el idioma de la aplicación y redirige a la página anterior.
     * El {@link org.springframework.web.servlet.i18n.LocaleChangeInterceptor}
     * intercepta el parámetro {@code lang} y actualiza la sesión automáticamente.
     *
     * @param lang    Código del idioma ("es" o "en").
     * @param request Petición HTTP para obtener la URL de origen.
     * @return Redirección a la página anterior.
     */
    @GetMapping("/lang")
    public String changeLanguage(@RequestParam String lang, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}