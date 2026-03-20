package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Vehicle.
 * Gestiona la persistencia de los vehículos, incluyendo validaciones críticas de unicidad
 * para matrículas y números de bastidor (VIN).
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // ──────────────────────────────────────────────────────────────────────────
    // Validaciones de Unicidad (Matrícula)
    // ──────────────────────────────────────────────────────────────────────────

    /** Verifica si una matrícula ya está registrada en el sistema. */
    boolean existsByMatricula(String matricula);

    /** Valida la matrícula durante la edición, excluyendo el registro actual. */
    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    // ──────────────────────────────────────────────────────────────────────────
    // Validaciones de Unicidad (VIN / Bastidor)
    // ──────────────────────────────────────────────────────────────────────────

    /** Verifica si un número de bastidor ya existe. */
    boolean existsByVin(String vin);

    /** Valida el VIN durante la edición, excluyendo el registro actual. */
    boolean existsByVinAndIdNot(String vin, Long id);

    // ──────────────────────────────────────────────────────────────────────────
    // Consultas Optimizadas
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Recupera un vehículo cargando su marca (Brand) de forma ansiosa (Eager).
     * Evita múltiples consultas SQL al acceder al nombre o país de la marca en los Mappers.
     * * @param id Identificador del vehículo.
     * @return Optional con el vehículo y su marca cargada.
     */
    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.brand WHERE v.id = :id")
    Optional<Vehicle> findByIdWithBrand(@Param("id") Long id);

    /**
     * Recupera todos los vehículos asociados a un usuario específico.
     * Fundamental para rellenar desplegables en la petición de citas del cliente.
     *
     * @param userId Identificador del usuario propietario.
     * @return Lista de vehículos pertenecientes al usuario.
     */
    List<Vehicle> findByUserId(Long userId);
}