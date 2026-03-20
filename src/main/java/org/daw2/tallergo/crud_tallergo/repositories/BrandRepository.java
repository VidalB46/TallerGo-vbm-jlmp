package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad Brand.
 * Proporciona métodos de acceso a datos para la gestión de marcas de vehículos.
 */
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    /**
     * Comprueba si ya existe una marca con el nombre especificado.
     * Útil para validaciones antes de la creación.
     */
    boolean existsByName(String name);

    /**
     * Comprueba si existe otra marca con el mismo nombre pero diferente ID.
     * Esencial para validaciones de unicidad durante la actualización.
     */
    boolean existsByNameAndIdNot(String name, Integer id);

    /**
     * Recupera una marca por su ID cargando ansiosamente (Eager) su colección de vehículos.
     * Utiliza un LEFT JOIN FETCH para evitar el problema de las N+1 consultas.
     * * @param id Identificador de la marca.
     * @return Optional con la marca y sus vehículos cargados.
     */
    @Query("SELECT b FROM Brand b LEFT JOIN FETCH b.vehicles WHERE b.id = :id")
    Optional<Brand> findByIdWithVehicles(@Param("id") Integer id);
}