package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.UserCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserUpdateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserRegisterDTO;
import org.daw2.tallergo.crud_tallergo.entities.Role;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.UserMapper;
import org.daw2.tallergo.crud_tallergo.repositories.RoleRepository;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Implementación del servicio de gestión de usuarios.
 * Centraliza la lógica de seguridad, hashing de contraseñas y asignación de roles.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int PASSWORD_EXPIRY_DAYS = 90;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Devuelve una página de usuarios ordenados según los parámetros de paginación.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de usuario.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toDTO);
    }

    /**
     * Busca usuarios cuyo email contenga el texto indicado (búsqueda parcial).
     *
     * @param q        Texto a buscar en el email del usuario.
     * @param pageable Configuración de paginación.
     * @return Página de DTOs de usuario que coinciden con la búsqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> search(String q, Pageable pageable) {
        return userRepository.searchByEmail(q, pageable).map(UserMapper::toDTO);
    }

    /**
     * Recupera los datos de un usuario formateados para el formulario de edición.
     *
     * @param id Identificador único del usuario.
     * @return DTO con los campos editables del usuario.
     * @throws ResourceNotFoundException si no existe ningún usuario con el ID indicado.
     */
    @Override
    @Transactional(readOnly = true)
    public UserUpdateDTO getForEdit(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserMapper.toUpdateDTO(user);
    }

    /**
     * Crea un nuevo usuario desde el panel de administración.
     * Genera una contraseña temporal aleatoria, la hashea y obliga al usuario
     * a cambiarla en el primer inicio de sesión.
     *
     * @param dto DTO con los datos del nuevo usuario, incluyendo roles e email.
     * @throws DuplicateResourceException si ya existe un usuario con el mismo email.
     */
    @Override
    public void create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }

        // Lógica de expiración de contraseña
        LocalDateTime now = LocalDateTime.now();
        dto.setLastPasswordChange(now);
        dto.setPasswordExpiresAt(now.plusDays(PASSWORD_EXPIRY_DAYS));

        // Generar contraseña temporal aleatoria y hashearla (el usuario deberá cambiarla)
        String tempPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String hashedPassword = passwordEncoder.encode(tempPassword);

        // Mapeo de roles desde IDs recibidos del formulario
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        User user = UserMapper.toEntity(dto, roles);
        user.setPasswordHash(hashedPassword);
        user.setMustChangePassword(true);
        userRepository.save(user);
        logger.info("Usuario creado por administrador: {} (contraseña temporal generada)", user.getEmail());
    }

    /**
     * Actualiza los datos de un usuario existente.
     * Si se proporcionan nuevos IDs de roles, estos reemplazan completamente a los actuales.
     *
     * @param dto DTO con los nuevos valores del usuario, incluyendo su ID.
     * @throws ResourceNotFoundException  si no existe ningún usuario con el ID indicado.
     * @throws DuplicateResourceException si el nuevo email ya está en uso por otro usuario.
     */
    @Override
    public void update(UserUpdateDTO dto) {
        User existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", dto.getId()));

        // Validar que el nuevo email no esté en uso por otro usuario
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }

        // Actualización manual de roles si vienen en el DTO
        if (dto.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
            existingUser.setRoles(roles);
        }

        UserMapper.copyToExistingEntity(dto, existingUser);
        userRepository.save(existingUser);
        logger.info("Usuario actualizado: {}", existingUser.getEmail());
    }

    /**
     * Elimina permanentemente un usuario del sistema por su ID.
     *
     * @param id Identificador único del usuario a eliminar.
     * @throws ResourceNotFoundException si no existe ningún usuario con el ID indicado.
     */
    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("user", "id", id);
        }
        userRepository.deleteById(id);
        logger.warn("Usuario con ID {} eliminado", id);
    }

    /**
     * Devuelve la vista detallada de un usuario junto con sus roles asignados.
     *
     * @param id Identificador único del usuario.
     * @return DTO con toda la información del usuario y sus roles.
     * @throws ResourceNotFoundException si no existe ningún usuario con el ID indicado.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO getDetail(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserMapper.toDetailDTO(user);
    }

    /**
     * Devuelve la lista de todos los roles disponibles en el sistema.
     *
     * @return Lista de roles registrados en base de datos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Flujo de autoregistro para clientes externos.
     * Aplica por defecto ROLE_CLIENT y encripta la contraseña con BCrypt.
     *
     * @param dto DTO con el email y contraseña introducidos por el usuario en el formulario de registro.
     * @throws DuplicateResourceException si ya existe un usuario con ese email.
     * @throws IllegalStateException      si el rol ROLE_CLIENT no está definido en la base de datos.
     */
    @Override
    public void registerNewClient(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);
        user.setAccountNonLocked(true);
        user.setEmailVerified(false);
        user.setLastPasswordChange(LocalDateTime.now());
        user.setPasswordExpiresAt(LocalDateTime.now().plusDays(PASSWORD_EXPIRY_DAYS));

        Role clientRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalStateException("Error: El rol ROLE_CLIENT no existe en la base de datos."));

        user.getRoles().add(clientRole);
        userRepository.save(user);
        logger.info("Nuevo cliente registrado: {}", user.getEmail());
    }
}