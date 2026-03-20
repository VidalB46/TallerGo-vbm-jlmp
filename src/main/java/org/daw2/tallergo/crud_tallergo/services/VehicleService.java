package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.VehicleCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interfaz de servicio para la gestión del inventario de vehículos.
 * Define las operaciones necesarias para administrar los coches que entran en el taller,
 * vinculándolos con sus respectivos propietarios y marcas.
 */
public interface VehicleService {

    /**
     * Recupera una página de vehículos para la vista de listado general.
     * @param pageable Parámetros de paginación y ordenación.
     * @return Página de objetos VehicleDTO.
     */
    Page<VehicleDTO> list(Pageable pageable);

    /**
     * Obtiene los datos de un vehículo preparados para cargar en el formulario de edición.
     * @param id Identificador único del vehículo.
     * @return DTO de actualización con los datos actuales.
     */
    VehicleUpdateDTO getForEdit(Long id);

    /**
     * Registra un nuevo vehículo en el sistema.
     * Debe validar la unicidad de la matrícula y el bastidor (VIN).
     * @param dto Datos de creación del vehículo.
     */
    void create(VehicleCreateDTO dto);

    /**
     * Actualiza la información técnica o el propietario de un vehículo existente.
     * @param dto Datos modificados del vehículo.
     */
    void update(VehicleUpdateDTO dto);

    /**
     * Elimina el registro de un vehículo del taller.
     * @param id Identificador del vehículo a borrar.
     */
    void delete(Long id);

    /**
     * Recupera la ficha completa de un vehículo, incluyendo su marca y detalles del dueño.
     * @param id Identificador único del vehículo.
     * @return DTO con el detalle exhaustivo.
     */
    VehicleDetailDTO getDetail(Long id);

    /**
     * Recupera todos los vehículos pertenecientes a un usuario específico.
     * Útil para rellenar desplegables cuando el cliente solicita una cita.
     * @param userId Identificador del usuario.
     * @return Lista de VehicleDTO.
     */
    List<VehicleDTO> getVehiclesByUserId(Long userId);
}