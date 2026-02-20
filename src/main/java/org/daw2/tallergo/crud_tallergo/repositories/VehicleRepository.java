package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByMatricula(String matricula);
    boolean existsByMatriculaAndIdNot(String matricula, Long id);
    boolean existsByVin(String vin);
    boolean existsByVinAndIdNot(String vin, Long id);

    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.brand WHERE v.id = :id")
    Optional<Vehicle> findByIdWithBrand(@Param("id") Long id);
}