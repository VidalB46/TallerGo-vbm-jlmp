package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Repair.
 * Maneja la persistencia de las órdenes de reparación en curso y finalizadas.
 */
@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

    /**
     * Verifica si ya existe una reparación vinculada a una cita específica.
     */
    boolean existsByAppointmentId(Long appointmentId);

    /**
     * Recupera una reparación con su vehículo y cita asociados en una única consulta.
     */
    @Query("SELECT r FROM Repair r " +
            "JOIN FETCH r.vehicle v " +
            "JOIN FETCH r.appointment a " +
            "WHERE r.id = :id")
    Optional<Repair> findByIdWithVehicleAndAppointment(@Param("id") Long id);
}