package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.UserCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserUpdateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserRegisterDTO;
import org.daw2.tallergo.crud_tallergo.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interfaz de servicio principal para la gestión de usuarios en TallerGo.
 * Orquesta tanto las operaciones administrativas (CRUD) como los flujos
 * públicos de registro de clientes.
 */
public interface UserService {

    /**
     * Lista paginada de usuarios para el panel de administración.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de usuario.
     */
    Page<UserDTO> list(Pageable pageable);

    /**
     * Búsqueda paginada de usuarios por email u otros campos.
     *
     * @param q        Texto a buscar.
     * @param pageable Configuración de paginación.
     * @return Página de DTOs de usuario coincidentes.
     */
    Page<UserDTO> search(String q, Pageable pageable);

    /**
     * Recupera los datos de un usuario preparados para su edición en formulario.
     *
     * @param id Identificador único del usuario.
     * @return DTO con los campos editables del usuario.
     */
    UserUpdateDTO getForEdit(Long id);

    /**
     * Crea un usuario desde el panel de administración (puede asignar roles específicos).
     *
     * @param dto DTO con los datos del nuevo usuario.
     */
    void create(UserCreateDTO dto);

    /**
     * Actualiza los datos de cuenta de un usuario existente.
     *
     * @param dto DTO con los nuevos valores e ID del usuario.
     */
    void update(UserUpdateDTO dto);

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id Identificador único del usuario a eliminar.
     */
    void delete(Long id);

    /**
     * Obtiene la vista detallada de un usuario, incluyendo roles y actividad.
     *
     * @param id Identificador único del usuario.
     * @return DTO de detalle con información completa del usuario.
     */
    UserDetailDTO getDetail(Long id);

    /**
     * Recupera todos los roles disponibles en el sistema (ROLE_USER, ROLE_ADMIN, etc.).
     * Útil para los desplegables en formularios de creación/edición.
     *
     * @return Lista de todos los roles existentes.
     */
    List<Role> findAllRoles();

    /**
     * Flujo de registro público para nuevos clientes.
     * Asigna el rol por defecto {@code ROLE_CLIENT} y prepara la cuenta inicial.
     *
     * @param dto DTO con los datos del cliente a registrar (email, contraseña, etc.).
     */
    void registerNewClient(UserRegisterDTO dto);
}