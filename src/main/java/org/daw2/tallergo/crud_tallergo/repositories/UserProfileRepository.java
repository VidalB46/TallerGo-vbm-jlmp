package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad UserProfile.
 * Maneja la persistencia de la información extendida de los usuarios (datos personales).
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Recupera el perfil asociado a un identificador de usuario específico.
     * Útil para vistas de "Mi Perfil" o cuando se necesita mostrar información
     * detallada del propietario de un vehículo.
     *
     * @param userId ID del usuario (coincide con el ID del perfil debido a MapsId).
     * @return Optional con el perfil del usuario.
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * Verifica si un usuario ya tiene un perfil creado.
     * Esencial para decidir si se debe realizar una operación de inserción o actualización
     * en el flujo de registro o primer inicio de sesión.
     *
     * @param userId ID del usuario a consultar.
     * @return true si el perfil existe.
     */
    boolean existsByUserId(Long userId);
}