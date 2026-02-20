package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);

    @Query("SELECT b FROM Brand b LEFT JOIN FETCH b.vehicles WHERE b.id = :id")
    Optional<Brand> findByIdWithVehicles(@Param("id") Integer id);
}