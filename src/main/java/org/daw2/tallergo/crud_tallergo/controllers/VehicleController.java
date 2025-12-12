package org.daw2.tallergo.crud_tallergo.controllers;

import jakarta.validation.Valid;
import org.daw2.tallergo.crud_tallergo.daos.BrandDAO;
import org.daw2.tallergo.crud_tallergo.daos.VehicleDAO;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.mappers.BrandMapper;
import org.daw2.tallergo.crud_tallergo.mappers.VehicleMapper;
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
@RequestMapping("/vehicles")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private VehicleDAO vehicleDAO;

    @Autowired
    private BrandDAO brandDAO;

    @GetMapping
    public String listVehicles(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "model") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model,
            Locale locale) {

        logger.info("Listando vehículos... page={}, size={}, sortField={}, sortDir={}",
                page, size, sortField, sortDir);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        try {
            long totalElements = vehicleDAO.countVehicles();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            if (totalPages > 0 && page >= totalPages) {
                page = totalPages - 1;
            }

            List<Vehicle> entities = vehicleDAO.listVehiclesPage(page, size, sortField, sortDir);
            List<VehicleDTO> dtos = VehicleMapper.toDTOList(entities);

            model.addAttribute("listVehicles", dtos);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalElements", totalElements);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc");

        } catch (Exception e) {
            logger.error("Error al listar vehículos: {}", e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.list.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }

        return "views/vehicle/vehicle-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {
        logger.info("Mostrando formulario nuevo vehículo.");
        try {
            // Cargamos la lista de marcas para el desplegable
            List<Brand> listBrands = brandDAO.listAllBrands();
            List<BrandDTO> listBrandsDTOs = BrandMapper.toDTOList(listBrands);

            model.addAttribute("vehicle", new VehicleCreateDTO());
            model.addAttribute("listBrands", listBrandsDTOs);
        } catch (Exception e) {
            logger.error("Error al cargar marcas: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "views/vehicle/vehicle-form";
    }

    @PostMapping("/insert")
    public String insertVehicle(@Valid @ModelAttribute("vehicle") VehicleCreateDTO vehicleDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model,
                                Locale locale) {

        logger.info("Insertando vehículo matrícula {}", vehicleDTO.getMatricula());
        try {
            if (result.hasErrors()) {
                // Recargar desplegable si hay errores
                List<Brand> listBrands = brandDAO.listAllBrands();
                List<BrandDTO> listBrandsDTOs = BrandMapper.toDTOList(listBrands);
                model.addAttribute("listBrands", listBrandsDTOs);
                return "views/vehicle/vehicle-form";
            }

            if (vehicleDAO.existsVehicleByMatricula(vehicleDTO.getMatricula())) {
                logger.warn("Matrícula {} ya existe.", vehicleDTO.getMatricula());
                String errorMessage = messageSource.getMessage("msg.vehicle-controller.insert.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/vehicles/new";
            }

            Vehicle vehicle = VehicleMapper.toEntity(vehicleDTO);
            vehicleDAO.insertVehicle(vehicle);
            logger.info("Vehículo insertado con ID: {}", vehicle.getId());

        } catch (Exception e) {
            logger.error("Error insertando vehículo: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Editando vehículo ID {}", id);
        try {
            Vehicle vehicle = vehicleDAO.getVehicleById(id);
            if (vehicle == null) {
                String errorMessage = messageSource.getMessage("msg.vehicle-controller.edit.notfound", null, locale);
                model.addAttribute("errorMessage", errorMessage);
                return "redirect:/vehicles";
            }

            VehicleUpdateDTO vehicleDTO = VehicleMapper.toUpdateDTO(vehicle);

            // Cargar marcas para el desplegable
            List<Brand> listBrands = brandDAO.listAllBrands();
            List<BrandDTO> listBrandsDTOs = BrandMapper.toDTOList(listBrands);

            model.addAttribute("vehicle", vehicleDTO);
            model.addAttribute("listBrands", listBrandsDTOs);

        } catch (Exception e) {
            logger.error("Error cargando vehículo {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "views/vehicle/vehicle-form";
    }

    @PostMapping("/update")
    public String updateVehicle(@Valid @ModelAttribute("vehicle") VehicleUpdateDTO vehicleDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model,
                                Locale locale) {

        logger.info("Actualizando vehículo ID {}", vehicleDTO.getId());
        try {
            if (result.hasErrors()) {
                List<Brand> listBrands = brandDAO.listAllBrands();
                List<BrandDTO> listBrandsDTOs = BrandMapper.toDTOList(listBrands);
                model.addAttribute("listBrands", listBrandsDTOs);
                return "views/vehicle/vehicle-form";
            }

            if (vehicleDAO.existsVehicleByMatriculaAndNotId(vehicleDTO.getMatricula(), vehicleDTO.getId())) {
                String errorMessage = messageSource.getMessage("msg.vehicle-controller.update.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/vehicles/edit?id=" + vehicleDTO.getId();
            }

            // Recuperamos la entidad original para no perder datos
            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleDTO.getId());
            if(vehicle != null) {
                VehicleMapper.copyToExistingEntity(vehicleDTO, vehicle);
                vehicleDAO.updateVehicle(vehicle);
            }

            logger.info("Vehículo actualizado.");

        } catch (Exception e) {
            logger.error("Error actualizando vehículo: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/vehicles";
    }

    @PostMapping("/delete")
    public String deleteVehicle(@RequestParam("id") Long id,
                                RedirectAttributes redirectAttributes,
                                Locale locale) {
        try {
            vehicleDAO.deleteVehicle(id);
            logger.info("Vehículo eliminado ID {}", id);
        } catch (Exception e) {
            logger.error("Error eliminando vehículo: {}", e.getMessage());
            String errorMessage = messageSource.getMessage("msg.vehicle-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        try {
            Vehicle vehicle = vehicleDAO.getVehicleById(id);
            if (vehicle == null) {
                String msg = messageSource.getMessage("msg.vehicle-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/vehicles";
            }

            VehicleDetailDTO vehicleDTO = VehicleMapper.toDetailDTO(vehicle);
            model.addAttribute("vehicle", vehicleDTO);
            return "views/vehicle/vehicle-detail";

        } catch (Exception e) {
            logger.error("Error detalle vehículo: {}", e.getMessage());
            String msg = messageSource.getMessage("msg.vehicle-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/vehicles";
        }
    }
}