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

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserUpdateDTO getForEdit(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserMapper.toUpdateDTO(user);
    }

    @Override
    public void create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }

        // Lógica de expiración de contraseña
        LocalDateTime now = LocalDateTime.now();
        dto.setLastPasswordChange(now);
        dto.setPasswordExpiresAt(now.plusDays(PASSWORD_EXPIRY_DAYS));

        // Mapeo de roles desde IDs recibidos del formulario
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        User user = UserMapper.toEntity(dto, roles);
        userRepository.save(user);
        logger.info("Usuario creado por administrador: {}", user.getEmail());
    }

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

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("user", "id", id);
        }
        userRepository.deleteById(id);
        logger.warn("Usuario con ID {} eliminado", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO getDetail(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserMapper.toDetailDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Flujo de autoregistro para clientes externos.
     * Aplica por defecto ROLE_CLIENT y encripta la contraseña con BCrypt.
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