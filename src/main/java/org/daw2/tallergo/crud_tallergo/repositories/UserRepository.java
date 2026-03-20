package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio central de seguridad y gestión de usuarios.
 * Este repositorio es la pieza clave para la autenticación de Spring Security y OAuth2.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recupera un usuario por su email cargando sus roles en una sola consulta.
     * La anotación @EntityGraph es una alternativa elegante a JOIN FETCH para
     * evitar el problema de las N+1 consultas en la capa de seguridad.
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    /**
     * Verifica si un correo ya está registrado.
     * Crucial para el CustomOAuth2SuccessHandler y procesos de registro.
     */
    boolean existsByEmail(String email);

    /**
     * Valida la unicidad del email al actualizar un perfil, excluyendo al usuario actual.
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Versión explícita con JPQL para obtener un usuario y sus roles por ID.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    /**
     * Búsqueda de email ignorando mayúsculas/minúsculas para mejorar la UX en el login.
     */
    Optional<User> findByEmailIgnoreCase(String email);
}