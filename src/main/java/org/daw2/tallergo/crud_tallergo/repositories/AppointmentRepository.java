package org.daw2.tallergo.crud_tallergo.repositories;

import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByUserId(Long userId, Pageable pageable);

    Page<Appointment> findByWorkshopId(Integer workshopId, Pageable pageable);

    @Query(
            value = """
            SELECT a
            FROM Appointment a
            JOIN FETCH a.user
            JOIN FETCH a.workshop
            JOIN FETCH a.vehicle
            WHERE a.user.id = :userId
            """,
            countQuery = """
            SELECT COUNT(a)
            FROM Appointment a
            WHERE a.user.id = :userId
            """
    )
    Page<Appointment> findByUserIdWithDetails(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = """
            SELECT a
            FROM Appointment a
            JOIN FETCH a.user
            JOIN FETCH a.workshop
            JOIN FETCH a.vehicle
            """,
            countQuery = """
            SELECT COUNT(a)
            FROM Appointment a
            """
    )
    Page<Appointment> findAllWithDetails(Pageable pageable);

    @Query("""
            SELECT a
            FROM Appointment a
            JOIN FETCH a.user
            JOIN FETCH a.workshop
            JOIN FETCH a.vehicle
            WHERE a.id = :id
            """)
    Optional<Appointment> findByIdWithDetails(@Param("id") Long id);

    @Query(
            value = """
            SELECT a
            FROM Appointment a
            JOIN FETCH a.user
            JOIN FETCH a.workshop
            JOIN FETCH a.vehicle
            WHERE a.user.id = :userId
              AND a.archivedByClient = false
            """,
            countQuery = """
            SELECT COUNT(a)
            FROM Appointment a
            WHERE a.user.id = :userId
              AND a.archivedByClient = false
            """
    )
    Page<Appointment> findActiveByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = """
            SELECT a
            FROM Appointment a
            JOIN FETCH a.user
            JOIN FETCH a.workshop
            JOIN FETCH a.vehicle
            WHERE a.user.id = :userId
            """,
            countQuery = """
            SELECT COUNT(a)
            FROM Appointment a
            WHERE a.user.id = :userId
            """
    )
    Page<Appointment> findFullHistoryByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = """
            SELECT *
            FROM appointments a
            WHERE a.user_id = :userId
              AND a.archived_by_client = false
            ORDER BY
              CASE
                WHEN a.status = 'LISTO_RECOGIDA' THEN 0
                WHEN a.status = 'SOLICITADO' THEN 1
                WHEN a.status = 'CONFIRMADO' THEN 2
                WHEN a.status = 'EN_TALLER' THEN 3
                WHEN a.status = 'EN_REPARACION' THEN 4
                WHEN a.status = 'RECOGIDO' THEN 5
                WHEN a.status = 'CANCELADO' THEN 6
                WHEN a.status = 'RECHAZADA' THEN 7
                ELSE 8
              END,
              a.start_date ASC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM appointments a
            WHERE a.user_id = :userId
              AND a.archived_by_client = false
            """,
            nativeQuery = true
    )
    Page<Appointment> findActiveByUserIdOrderByBusinessStatus(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = """
            SELECT *
            FROM appointments a
            WHERE a.workshop_id = :workshopId
            ORDER BY
              CASE
                WHEN a.status = 'LISTO_RECOGIDA' THEN 0
                WHEN a.status = 'SOLICITADO' THEN 1
                WHEN a.status = 'CONFIRMADO' THEN 2
                WHEN a.status = 'EN_TALLER' THEN 3
                WHEN a.status = 'EN_REPARACION' THEN 4
                WHEN a.status = 'RECOGIDO' THEN 5
                WHEN a.status = 'CANCELADO' THEN 6
                WHEN a.status = 'RECHAZADA' THEN 7
                ELSE 8
              END,
              a.start_date ASC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM appointments a
            WHERE a.workshop_id = :workshopId
            """,
            nativeQuery = true
    )
    Page<Appointment> findByWorkshopIdOrderByBusinessStatus(@Param("workshopId") Integer workshopId, Pageable pageable);

    @Query(
            value = """
            SELECT *
            FROM appointments a
            ORDER BY
              CASE
                WHEN a.status = 'LISTO_RECOGIDA' THEN 0
                WHEN a.status = 'SOLICITADO' THEN 1
                WHEN a.status = 'CONFIRMADO' THEN 2
                WHEN a.status = 'EN_TALLER' THEN 3
                WHEN a.status = 'EN_REPARACION' THEN 4
                WHEN a.status = 'RECOGIDO' THEN 5
                WHEN a.status = 'CANCELADO' THEN 6
                WHEN a.status = 'RECHAZADA' THEN 7
                ELSE 8
              END,
              a.start_date ASC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM appointments a
            """,
            nativeQuery = true
    )
    Page<Appointment> findAllOrderByBusinessStatus(Pageable pageable);
}