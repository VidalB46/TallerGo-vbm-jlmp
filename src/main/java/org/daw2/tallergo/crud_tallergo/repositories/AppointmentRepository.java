package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Appointment.
 * Gestiona el ciclo de vida de las citas y permite la recuperación eficiente
 * de las relaciones con clientes, vehículos y talleres.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Recupera todas las citas de un usuario específico de forma paginada.
     * Útil para la vista "Mis Citas" del cliente.
     */
    Page<Appointment> findByUserId(Long userId, Pageable pageable);

    /**
     * Recupera todas las citas programadas para un taller específico.
     * Esencial para la agenda de trabajo de los mecánicos y administradores.
     */
    Page<Appointment> findByWorkshopId(Integer workshopId, Pageable pageable);

    /**
     * Recupera una cita cargando ansiosamente (Eager) sus relaciones principales.
     * Evita el problema de las N+1 consultas al mostrar el detalle completo.
     *
     * @param id Identificador de la cita.
     * @return Optional con la cita y sus entidades relacionadas cargadas.
     */
    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.user " +
            "JOIN FETCH a.workshop " +
            "JOIN FETCH a.vehicle " +
            "WHERE a.id = :id")
    Optional<Appointment> findByIdWithDetails(@Param("id") Long id);
}