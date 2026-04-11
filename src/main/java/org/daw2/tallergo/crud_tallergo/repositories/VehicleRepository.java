package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Vehicle.
 * Gestiona la persistencia de los vehículos, incluyendo validaciones de unicidad,
 * consultas optimizadas y filtrado por propietario.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // ──────────────────────────────────────────────────────────────────────────
    // Validaciones de Unicidad
    // ──────────────────────────────────────────────────────────────────────────

    /** Verifica si una matrícula ya está registrada en el sistema. */
    boolean existsByMatricula(String matricula);

    /** Valida la matrícula durante la edición, excluyendo el registro actual. */
    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    /** Verifica si un número de bastidor (VIN) ya existe. */
    boolean existsByVin(String vin);

    /** Valida el VIN durante la edición, excluyendo el registro actual. */
    boolean existsByVinAndIdNot(String vin, Long id);

    // ──────────────────────────────────────────────────────────────────────────
    // Consultas por Propietario
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Recupera todos los vehículos asociados a un usuario específico.
     * Útil para rellenar selectores en la creación de citas.
     *
     * @param userId Identificador del usuario propietario.
     * @return Lista de vehículos pertenecientes al usuario.
     */
    List<Vehicle> findByUserId(Long userId);

    /**
     * Recupera una página de vehículos pertenecientes a un usuario específico.
     * Utilizado para el listado principal de vehículos del cliente.
     *
     * @param userId Identificador del usuario.
     * @param pageable Configuración de paginación y ordenamiento.
     * @return Página de vehículos filtrados.
     */
    Page<Vehicle> findByUserId(Long userId, Pageable pageable);

    // ──────────────────────────────────────────────────────────────────────────
    // Consultas Optimizadas (Fetch Joins)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Recupera un vehículo cargando su marca (Brand) de forma ansiosa (Eager).
     * Evita el problema de las N+1 consultas al acceder a datos de la marca.
     * * @param id Identificador del vehículo.
     * @return Optional con el vehículo y su marca asociada.
     */
    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.brand WHERE v.id = :id")
    Optional<Vehicle> findByIdWithBrand(@Param("id") Long id);
}