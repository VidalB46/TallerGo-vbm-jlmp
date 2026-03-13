package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.dtos.UserCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserUpdateDTO;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Controlador para la gestión de usuarios.
 * Permite listar, crear, editar, actualizar, eliminar y ver detalles de usuarios.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista los usuarios con paginación y ordenamiento por email.
     *
     * @param pageable Configuración de paginación y ordenamiento.
     * @param model    Modelo para pasar atributos a la vista.
     * @return Vista del listado de usuarios ("views/user/user-list").
     */
    @GetMapping
    public String listUsers(
            @PageableDefault(size = 10, sort = "email", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        try {
            Page<UserDTO> page = userService.list(pageable);
            model.addAttribute("page", page);

            String sortParam = "email,asc";
            if (page.getSort().isSorted()) {
                Sort.Order order = page.getSort().iterator().next();
                sortParam = order.getProperty() + "," + order.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al listar los usuarios.");
        }
        return "views/user/user-list";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     * Incluye la lista de roles disponibles.
     *
     * @param model Modelo para pasar atributos a la vista.
     * @return Vista del formulario de usuario ("views/user/user-form").
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        model.addAttribute("allRoles", userService.findAllRoles());
        return "views/user/user-form";
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     * Valida el formulario y maneja errores de duplicidad de email.
     *
     * @param userDTO            DTO con los datos del usuario a crear.
     * @param result             Resultado de la validación del formulario.
     * @param model              Modelo para recargar atributos en caso de error.
     * @param redirectAttributes Atributos flash para mensajes de éxito o error.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección al listado de usuarios o recarga del formulario si hay errores.
     */
    @PostMapping("/insert")
    public String insertUser(@Valid @ModelAttribute("user") UserCreateDTO userDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("allRoles", userService.findAllRoles());
            return "views/user/user-form";
        }

        try {
            userService.create(userDTO);
            return "redirect:/users";
        } catch (DuplicateResourceException ex) {
            String errorMessage = messageSource.getMessage("msg.user-controller.insert.emailExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/users/new";
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.user-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/users/new";
        }
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     *
     * @param id                 ID del usuario a editar.
     * @param model              Modelo para pasar atributos a la vista.
     * @param redirectAttributes Atributos flash para mensajes.
     * @param locale             Configuración regional para mensajes.
     * @return Vista del formulario de edición o redirección si ocurre un error.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            UserUpdateDTO userDTO = userService.getForEdit(id);
            model.addAttribute("user", userDTO);
            model.addAttribute("allRoles", userService.findAllRoles());
            return "views/user/user-form";
        } catch (Exception e) {
            String msg = messageSource.getMessage("msg.user-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users";
        }
    }

    /**
     * Actualiza un usuario existente.
     * Maneja errores de validación y duplicidad de email.
     *
     * @param userDTO            DTO con los datos actualizados.
     * @param result             Resultado de la validación.
     * @param redirectAttributes Atributos flash para mensajes.
     * @param model              Modelo para recargar datos en caso de error.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección al listado de usuarios o recarga del formulario si hay errores.
     */
    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDTO userDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("allRoles", userService.findAllRoles());
            return "views/user/user-form";
        }

        try {
            userService.update(userDTO);
            return "redirect:/users";
        } catch (DuplicateResourceException ex) {
            String errorMessage = messageSource.getMessage("msg.user-controller.update.emailExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/users/edit?id=" + userDTO.getId();
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("msg.user-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/users/edit?id=" + userDTO.getId();
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id                 ID del usuario a eliminar.
     * @param redirectAttributes Atributos flash para mensajes.
     * @param locale             Configuración regional para mensajes.
     * @return Redirección al listado de usuarios.
     */
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            userService.delete(id);
            return "redirect:/users";
        } catch (Exception e) {
            String msg = messageSource.getMessage("msg.user-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users";
        }
    }

    /**
     * Muestra los detalles de un usuario.
     *
     * @param id                 ID del usuario.
     * @param model              Modelo para pasar atributos a la vista.
     * @param redirectAttributes Atributos flash para mensajes.
     * @param locale             Configuración regional.
     * @return Vista con detalle de usuario o redirección al listado si hay error.
     */
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            UserDetailDTO userDetailDTO = userService.getDetail(id);
            model.addAttribute("user", userDetailDTO);
            return "views/user/user-detail";
        } catch (Exception e) {
            return "redirect:/users";
        }
    }
}