package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.daos.MecanicoDAO;
import org.daw2.tallergo.crud_tallergo.daos.TallerDAO;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Mecanico;
import org.daw2.tallergo.crud_tallergo.mappers.MecanicoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/mecanicos")
public class MecanicoController {

    private static final Logger logger = LoggerFactory.getLogger(MecanicoController.class);

    @Autowired
    private MecanicoDAO mecanicoDAO;

    @Autowired
    private TallerDAO tallerDAO;

    @Autowired
    private MessageSource messageSource;

    // ────────────────────────────────
    // LISTADO
    // ────────────────────────────────
    @GetMapping
    public String listMecanicos(Model model, Locale locale) {
        try {
            List<Mecanico> mecanicos = mecanicoDAO.listAll();
            model.addAttribute("listMecanicos", MecanicoMapper.toDTOList(mecanicos));
        } catch (Exception e) {
            logger.error("Error al listar mecánicos", e);
            String msg = messageSource.getMessage("msg.mecanico.list.error", null, locale);
            model.addAttribute("errorMessage", msg);
        }
        return "views/mecanico/mecanico-list";
    }

    // ────────────────────────────────
    // DETALLE
    // ────────────────────────────────
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        try {
            Mecanico mecanico = mecanicoDAO.getById(id);
            if (mecanico == null) {
                String msg = messageSource.getMessage("msg.mecanico.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/mecanicos";
            }
            MecanicoDetailDTO dto = MecanicoMapper.toDetailDTO(mecanico);
            model.addAttribute("mecanico", dto);
        } catch (Exception e) {
            logger.error("Error al mostrar detalle del mecánico {}", id, e);
            String msg = messageSource.getMessage("msg.mecanico.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/mecanicos";
        }
        return "views/mecanico/mecanico-detail";
    }


    // ────────────────────────────────
    // CREACIÓN
    // ────────────────────────────────
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("mecanico", new MecanicoCreateDTO());
        model.addAttribute("talleres", tallerDAO.listAll());
        return "views/mecanico/mecanico-form";
    }

    @PostMapping("/insert")
    public String insertMecanico(@Valid @ModelAttribute("mecanico") MecanicoCreateDTO dto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("talleres", tallerDAO.listAll());
            return "views/mecanico/mecanico-form";
        }

        // Inserta el mecánico directamente
        Mecanico mecanico = MecanicoMapper.toEntity(dto);
        mecanicoDAO.insert(mecanico);

        // Mensaje de éxito
        String msg = messageSource.getMessage("msg.mecanico.insert.success", null, locale);
        redirectAttributes.addFlashAttribute("successMessage", msg);

        return "redirect:/mecanicos";
    }


    // ────────────────────────────────
    // EDICIÓN
    // ────────────────────────────────
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {
        try {
            Mecanico mecanico = mecanicoDAO.getById(id);
            if (mecanico == null) {
                String msg = messageSource.getMessage("msg.mecanico.edit.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/mecanicos";
            }

            MecanicoUpdateDTO dto = MecanicoMapper.toUpdateDTO(mecanico);
            model.addAttribute("mecanico", dto);
            model.addAttribute("talleres", tallerDAO.listAll());

        } catch (Exception e) {
            logger.error("Error al cargar edición del mecánico {}", id, e);
            String msg = messageSource.getMessage("msg.mecanico.edit.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/mecanicos";
        }
        return "views/mecanico/mecanico-form";
    }

    @PostMapping("/update")
    public String updateMecanico(@Valid @ModelAttribute("mecanico") MecanicoUpdateDTO dto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("talleres", tallerDAO.listAll());
            return "views/mecanico/mecanico-form";
        }

        try {
            Mecanico mecanico = mecanicoDAO.getById(dto.getId());
            if (mecanico == null) {
                String msg = messageSource.getMessage("msg.mecanico.edit.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/mecanicos";
            }

            MecanicoMapper.copyToExistingEntity(dto, mecanico);
            mecanicoDAO.update(mecanico);

            String msg = messageSource.getMessage("msg.mecanico.update.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", msg);

        } catch (Exception e) {
            logger.error("Error al actualizar mecánico {}", dto.getId(), e);
            String msg = messageSource.getMessage("msg.mecanico.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
        }

        return "redirect:/mecanicos";
    }

    // ────────────────────────────────
    // ELIMINACIÓN
    // ────────────────────────────────
    @PostMapping("/delete")
    public String deleteMecanico(@RequestParam("id") Long id,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {
        try {
            mecanicoDAO.delete(id);
            String msg = messageSource.getMessage("msg.mecanico.delete.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            logger.error("Error al eliminar mecánico {}", id, e);
            String msg = messageSource.getMessage("msg.mecanico.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
        }
        return "redirect:/mecanicos";
    }
}