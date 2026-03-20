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
     */
    Page<UserDTO> list(Pageable pageable);

    /**
     * Recupera los datos de un usuario preparados para su edición.
     */
    UserUpdateDTO getForEdit(Long id);

    /**
     * Crea un usuario desde el panel de administración (pudiendo asignar roles específicos).
     */
    void create(UserCreateDTO dto);

    /**
     * Actualiza los datos de cuenta de un usuario existente.
     */
    void update(UserUpdateDTO dto);

    /**
     * Elimina un usuario del sistema.
     */
    void delete(Long id);

    /**
     * Obtiene la vista detallada de un usuario, incluyendo roles y actividad.
     */
    UserDetailDTO getDetail(Long id);

    /**
     * Recupera todos los roles disponibles en el sistema (ROLE_USER, ROLE_ADMIN, etc.).
     * Útil para los desplegables en formularios de creación/edición.
     */
    List<Role> findAllRoles();

    /**
     * Flujo de registro público para nuevos clientes.
     * Implementa la lógica de asignación de rol por defecto (ROLE_USER) y
     * preparación inicial de la cuenta.
     */
    void registerNewClient(UserRegisterDTO dto);
}