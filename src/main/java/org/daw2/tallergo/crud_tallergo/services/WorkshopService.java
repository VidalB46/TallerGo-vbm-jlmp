package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de talleres (Workshops).
 * Define las operaciones necesarias para administrar los centros físicos del sistema.
 */
public interface WorkshopService {

    /**
     * Recupera una página de talleres para su visualización en el panel de administración.
     * @param pageable Configuración de paginación y ordenación.
     * @return Página de WorkshopDTO.
     */
    Page<WorkshopDTO> list(Pageable pageable);

    /**
     * Recupera la lista completa de talleres.
     * Útil para rellenar desplegables en formularios de alta de mecánicos o servicios.
     * @return Lista de todos los talleres.
     */
    List<WorkshopDTO> listAll();

    /**
     * Registra un nuevo taller en el sistema.
     * Debe validar que el NIF no esté duplicado.
     * @param dto Datos de creación del taller.
     */
    void create(WorkshopCreateDTO dto);

    /**
     * Obtiene el detalle completo de un taller, incluyendo su personal (mecánicos).
     * @param id Identificador único del taller.
     * @return DTO con información detallada.
     */
    WorkshopDetailDTO getDetail(Integer id);

    /**
     * Elimina un taller del sistema.
     * @param id Identificador único del taller a borrar.
     */
    void delete(Integer id);
}