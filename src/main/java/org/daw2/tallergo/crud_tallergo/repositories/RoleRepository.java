package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Role.
 * Gestiona los permisos y perfiles de acceso (ROLE_USER, ROLE_ADMIN, etc.) dentro del sistema.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol específico por su nombre único.
     * Esencial durante el proceso de registro de nuevos usuarios para asignar
     * el rol por defecto o durante la gestión de permisos por parte de administradores.
     * * @param name Nombre del rol (ej: "ROLE_ADMIN").
     * @return Optional con el objeto Role si existe.
     */
    Optional<Role> findByName(String name);
}