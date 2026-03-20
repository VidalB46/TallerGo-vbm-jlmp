package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.BrandCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interfaz de servicio para la gestión de marcas (Brands).
 * Define las operaciones de negocio permitidas, desacoplando los controladores
 * de la implementación técnica de la persistencia.
 */
public interface BrandService {

    /**
     * Recupera una página de marcas para visualización en tablas con paginación.
     * @param pageable Configuración de paginación y ordenación.
     * @return Página de DTOs simplificados.
     */
    Page<BrandDTO> list(Pageable pageable);

    /**
     * Recupera todas las marcas sin paginar.
     * Ideal para componentes UI como Selects o Dropdowns al crear/editar vehículos.
     * @return Lista completa de marcas en formato DTO.
     */
    List<BrandDTO> listAll();

    /**
     * Obtiene los datos de una marca preparados específicamente para un formulario de edición.
     * @param id Identificador de la marca.
     * @return DTO de actualización con los datos actuales.
     */
    BrandUpdateDTO getForEdit(Integer id);

    /**
     * Registra una nueva marca en el sistema tras validar sus campos.
     * @param dto Datos de creación.
     */
    void create(BrandCreateDTO dto);

    /**
     * Actualiza los datos de una marca existente.
     * @param dto Datos modificados (debe incluir el ID).
     */
    void update(BrandUpdateDTO dto);

    /**
     * Elimina una marca del sistema por su identificador.
     * @param id Identificador de la marca a borrar.
     */
    void delete(Integer id);

    /**
     * Recupera la información detallada de una marca, incluyendo su relación con vehículos.
     * @param id Identificador de la marca.
     * @return DTO de detalle completo.
     */
    BrandDetailDTO getDetail(Integer id);
}