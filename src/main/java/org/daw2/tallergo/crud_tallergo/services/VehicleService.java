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
 * Soporta operaciones tanto globales (administrador) como filtradas por propietario (cliente).
 */
public interface VehicleService {

    /**
     * Devuelve una página paginada con todos los vehículos del sistema.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de vehículo.
     */
    Page<VehicleDTO> list(Pageable pageable);

    /**
     * Devuelve los vehículos de un usuario concreto con paginación.
     *
     * @param userId   ID del propietario.
     * @param pageable Configuración de paginación.
     * @return Página de DTOs de vehículo filtrados por usuario.
     */
    Page<VehicleDTO> listByUser(Long userId, Pageable pageable);

    /**
     * Recupera los datos de un vehículo para el formulario de edición.
     *
     * @param id Identificador único del vehículo.
     * @return DTO con los campos editables del vehículo.
     */
    VehicleUpdateDTO getForEdit(Long id);

    /**
     * Crea un nuevo vehículo y lo asocia al usuario indicado.
     *
     * @param dto    DTO con los datos del vehículo.
     * @param userId Identificador del propietario.
     */
    void create(VehicleCreateDTO dto, Long userId);

    /**
     * Actualiza los datos de un vehículo existente.
     *
     * @param dto DTO con los nuevos valores e ID del vehículo.
     */
    void update(VehicleUpdateDTO dto);

    /**
     * Elimina un vehículo por su ID.
     *
     * @param id Identificador único del vehículo.
     */
    void delete(Long id);

    /**
     * Devuelve el detalle completo de un vehículo con su marca cargada.
     *
     * @param id Identificador único del vehículo.
     * @return DTO completo con información de marca.
     */
    VehicleDetailDTO getDetail(Long id);

    /**
     * Devuelve todos los vehículos de un usuario como lista sin paginar.
     * Usado para selectores en formularios de citas.
     *
     * @param userId Identificador del propietario.
     * @return Lista de DTOs de vehículo del usuario.
     */
    List<VehicleDTO> getVehiclesByUserId(Long userId);

    /**
     * Búsqueda global (administrador) por modelo, matrícula o VIN con paginación.
     *
     * @param q        Texto a buscar.
     * @param pageable Configuración de paginación.
     * @return Página de resultados coincidentes.
     */
    Page<VehicleDTO> search(String q, Pageable pageable);

    /**
     * Búsqueda filtrada por propietario en modelo, matrícula o VIN con paginación.
     *
     * @param q        Texto a buscar.
     * @param userId   Identificador del propietario.
     * @param pageable Configuración de paginación.
     * @return Página de resultados del usuario coincidentes.
     */
    Page<VehicleDTO> searchByUser(String q, Long userId, Pageable pageable);
}