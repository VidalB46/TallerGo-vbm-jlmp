package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.UserCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.UserUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
    Page<UserDTO> list(Pageable pageable);
    UserUpdateDTO getForEdit(Long id);
    void create(UserCreateDTO dto);
    void update(UserUpdateDTO dto);
    void delete(Long id);
    UserDetailDTO getDetail(Long id);
    List<Role> findAllRoles();
}