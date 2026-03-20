package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz de servicio para la gestión de mecánicos.
 * Define las operaciones necesarias para administrar el personal técnico de los talleres.
 */
public interface MechanicService {

    /**
     * Recupera una página de mecánicos con su información básica.
     * @param pageable Configuración de paginación y ordenación.
     * @return Página de DTOs de mecánicos.
     */
    Page<MechanicDTO> list(Pageable pageable);

    /**
     * Registra un nuevo mecánico en un taller específico.
     * @param dto Datos de creación (incluye el workshopId).
     */
    void create(MechanicCreateDTO dto);

    /**
     * Elimina a un mecánico del sistema.
     * @param id Identificador único del mecánico.
     */
    void delete(Long id);
}