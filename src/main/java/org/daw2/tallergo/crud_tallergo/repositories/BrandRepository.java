package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    /** Búsqueda por nombre (parcial, case-insensitive). */
    @Query(value = "SELECT b FROM Brand b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%'))",
           countQuery = "SELECT COUNT(b) FROM Brand b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Brand> searchByName(@Param("q") String q, Pageable pageable);

    /** Filtro exacto por país (case-insensitive). */
    @Query(value = "SELECT b FROM Brand b WHERE LOWER(b.country) = LOWER(:country)",
           countQuery = "SELECT COUNT(b) FROM Brand b WHERE LOWER(b.country) = LOWER(:country)")
    Page<Brand> filterByCountry(@Param("country") String country, Pageable pageable);

    /** Búsqueda combinada: nombre + país. */
    @Query(value = "SELECT b FROM Brand b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%')) AND LOWER(b.country) = LOWER(:country)",
           countQuery = "SELECT COUNT(b) FROM Brand b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%')) AND LOWER(b.country) = LOWER(:country)")
    Page<Brand> searchByNameAndCountry(@Param("q") String q, @Param("country") String country, Pageable pageable);

    /** Lista de países distintos para el selector de filtro. */
    @Query(value = "SELECT DISTINCT b.country FROM brands b WHERE b.country IS NOT NULL ORDER BY b.country", nativeQuery = true)
    List<String> findDistinctCountries();
}