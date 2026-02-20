package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.UserCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserUpdateDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int PASSWORD_EXPIRY_DAYS = 90;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<UserDTO> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toDTO);
    }

    @Override
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
        LocalDateTime lastPasswordChange = dto.getLastPasswordChange();
        if (lastPasswordChange == null) {
            lastPasswordChange = LocalDateTime.now();
            dto.setLastPasswordChange(lastPasswordChange);
        }
        dto.setPasswordExpiresAt(lastPasswordChange.plusDays(PASSWORD_EXPIRY_DAYS));
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
        User user = UserMapper.toEntity(dto, roles);
        userRepository.save(user);
    }

    @Override
    public void update(UserUpdateDTO dto) {
        User existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", dto.getId()));

        if (!existingUser.getEmail().equalsIgnoreCase(dto.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }

        if (dto.getLastPasswordChange() == null) {
            dto.setLastPasswordChange(LocalDateTime.now());
        }
        dto.setPasswordExpiresAt(dto.getLastPasswordChange().plusDays(PASSWORD_EXPIRY_DAYS));

        if (dto.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
            existingUser.setRoles(roles);
        }

        UserMapper.copyToExistingEntity(dto, existingUser);
        userRepository.save(existingUser);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("user", "id", id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDetailDTO getDetail(Long id) {
        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserMapper.toDetailDTO(user);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}