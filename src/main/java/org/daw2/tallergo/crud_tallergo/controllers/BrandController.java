package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.daos.BrandDAO;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.mappers.BrandMapper;
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
@RequestMapping("/brands")
public class BrandController {

    private static final Logger logger = LoggerFactory.getLogger(BrandController.class);

    @Autowired
    private BrandDAO brandDAO;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listBrands(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "name") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model) {

        logger.info("Listando marcas page={}, size={}, sortField={}, sortDir={}",
                page, size, sortField, sortDir);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        try {
            long totalElements = brandDAO.countBrands();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            if (totalPages > 0 && page >= totalPages) {
                page = totalPages - 1;
            }

            List<Brand> listBrands = brandDAO.listBrandsPage(page, size, sortField, sortDir);
            List<BrandDTO> listBrandsDTOs = BrandMapper.toDTOList(listBrands);

            model.addAttribute("listBrands", listBrandsDTOs);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalElements", totalElements);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc");

        } catch (Exception e) {
            logger.error("Error al listar las marcas: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las marcas.");
        }

        return "views/brand/brand-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva marca.");
        model.addAttribute("brand", new BrandCreateDTO());
        return "views/brand/brand-form";
    }

    @PostMapping("/insert")
    public String insertBrand(@Valid @ModelAttribute("brand") BrandCreateDTO brandDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Locale locale) {

        logger.info("Insertando nueva marca con nombre {}", brandDTO.getName());
        try {
            if (result.hasErrors()) {
                return "views/brand/brand-form";
            }
            if (brandDAO.existsBrandByName(brandDTO.getName())) {
                logger.warn("El nombre de la marca {} ya existe.", brandDTO.getName());
                String errorMessage = messageSource.getMessage("msg.brand-controller.insert.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/brands/new";
            }
            Brand brand = BrandMapper.toEntity(brandDTO);
            brandDAO.insertBrand(brand);
            logger.info("Marca {} insertada con éxito.", brand.getName());
        } catch (Exception e) {
            logger.error("Error al insertar la marca {}: {}", brandDTO.getName(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.brand-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/brands";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Integer id, Model model) {
        logger.info("Mostrando formulario de edición para la marca con ID {}", id);
        try {
            Brand brand = brandDAO.getBrandById(id);
            if (brand == null) {
                logger.warn("No se encontró la marca con ID {}", id);
                return "redirect:/brands";
            }
            BrandUpdateDTO brandDTO = BrandMapper.toUpdateDTO(brand);
            model.addAttribute("brand", brandDTO);
        } catch (Exception e) {
            logger.error("Error al obtener la marca con ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la marca.");
        }
        return "views/brand/brand-form";
    }

    @PostMapping("/update")
    public String updateBrand(@Valid @ModelAttribute("brand") BrandUpdateDTO brandDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Locale locale) {

        logger.info("Actualizando marca con ID {}", brandDTO.getId());
        try {
            if (result.hasErrors()) {
                return "views/brand/brand-form";
            }

            if (brandDAO.existsBrandByNameAndNotId(brandDTO.getName(), brandDTO.getId())) {
                logger.warn("El nombre de la marca {} ya existe para otra marca.", brandDTO.getName());
                String errorMessage = messageSource.getMessage("msg.brand-controller.update.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/brands/edit?id=" + brandDTO.getId();
            }

            Brand brand = brandDAO.getBrandById(brandDTO.getId());
            if (brand == null){
                // Manejo de error si no existe
                return "redirect:/brands";
            }
            // Actualizamos los campos usando el Mapper
            BrandMapper.copyToExistingEntity(brandDTO, brand);

            brandDAO.updateBrand(brand);
            logger.info("Marca con ID {} actualizada con éxito.", brand.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar la marca con ID {}: {}", brandDTO.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.brand-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/brands";
    }

    @PostMapping("/delete")
    public String deleteBrand(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando marca con ID {}", id);
        try {
            brandDAO.deleteBrand(id);
            logger.info("Marca con ID {} eliminada con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la marca con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la marca.");
        }
        return "redirect:/brands";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        logger.info("Mostrando detalle de la marca con ID {}", id);
        try {
            Brand brand = brandDAO.getBrandById(id);
            if (brand == null) {
                String msg = messageSource.getMessage("msg.brand-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/brands";
            }
            BrandDetailDTO brandDTO = BrandMapper.toDetailDTO(brand);
            model.addAttribute("brand", brandDTO);
            return "views/brand/brand-detail";
        } catch (Exception e) {
            logger.error("Error al obtener detalle de marca {}: {}", id, e.getMessage());
            String msg = messageSource.getMessage("msg.brand-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/brands";
        }
    }
}
