package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;
import org.daw2.tallergo.crud_tallergo.mappers.RepairMapper;
import org.daw2.tallergo.crud_tallergo.repositories.AppointmentRepository;
import org.daw2.tallergo.crud_tallergo.repositories.RepairRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementación de los servicios de reparación con lógica de sincronización de estados.
 * Cuando el estado de una reparación cambia, se actualiza automáticamente el estado de la
 * cita asociada para mantener coherencia en el flujo de trabajo del taller.
 */
@Service
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * Devuelve el detalle completo de una reparación, incluyendo vehículo y cita asociados.
     *
     * @param id Identificador único de la reparación.
     * @return DTO de detalle con vehículo y cita cargados.
     * @throws IllegalArgumentException si no existe ninguna reparación con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public RepairDetailDTO getRepairById(Long id) {
        Repair repair = repairRepository.findByIdWithVehicleAndAppointment(id)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));
        return RepairMapper.toDetailDTO(repair);
    }

    /**
     * Devuelve una página paginada con todas las reparaciones del sistema.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de reparación.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RepairDTO> getAllRepairs(Pageable pageable) {
        return repairRepository.findAll(pageable).map(RepairMapper::toDTO);
    }

    /**
     * Actualiza los datos editables de una reparación existente.
     *
     * @param dto DTO con los nuevos valores y el ID de la reparación.
     * @return DTO actualizado de la reparación.
     * @throws IllegalArgumentException si no existe ninguna reparación con ese ID.
     */
    @Override
    @Transactional
    public RepairDTO updateRepair(RepairUpdateDTO dto) {
        Repair repair = repairRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));
        RepairMapper.updateEntity(dto, repair);
        return RepairMapper.toDTO(repairRepository.save(repair));
    }

    /**
     * Actualiza el estado de una reparación y sincroniza el estado de la cita asociada.
     * <ul>
     *   <li>{@code ACTIVO} → cita pasa a {@code EN_REPARACION}</li>
     *   <li>{@code FINALIZADO} → cita pasa a {@code LISTO_RECOGIDA}</li>
     * </ul>
     *
     * @param id        Identificador único de la reparación.
     * @param newStatus Nuevo estado a asignar.
     * @throws IllegalArgumentException si no existe ninguna reparación con ese ID.
     */
    @Override
    @Transactional
    public void updateStatus(Long id, RepairStatus newStatus) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));
        repair.setStatus(newStatus);
        repairRepository.save(repair);

        Appointment appointment = repair.getAppointment();
        if (appointment != null) {
            if (newStatus == RepairStatus.ACTIVO) {
                appointment.setStatus(AppointmentStatus.EN_REPARACION);
            } else if (newStatus == RepairStatus.FINALIZADO) {
                appointment.setStatus(AppointmentStatus.LISTO_RECOGIDA);
            }
            appointmentRepository.save(appointment);
        }
    }

    /**
     * Registra la entrega del vehículo al cliente y actualiza la cita a estado {@code RECOGIDO}.
     *
     * @param id Identificador único de la reparación cuyo vehículo se entrega.
     * @throws IllegalArgumentException si no existe ninguna reparación con ese ID.
     */
    @Override
    @Transactional
    public void deliverVehicle(Long id) {
        Repair repair = repairRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));
        Appointment appointment = repair.getAppointment();
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.RECOGIDO);
            appointmentRepository.save(appointment);
        }
    }

    /**
     * Crea automáticamente una reparación en estado {@code STANDBY} al confirmar una cita.
     * Si ya existe una reparación para la cita indicada, no hace nada.
     *
     * @param appointmentId Identificador de la cita para la que se genera la reparación.
     * @throws IllegalArgumentException si no existe ninguna cita con ese ID.
     */
    @Override
    @Transactional
    public void createAutomaticRepair(Long appointmentId) {
        if (repairRepository.existsByAppointmentId(appointmentId)) return;

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        Repair repair = new Repair();
        repair.setAppointment(appointment);
        repair.setVehicle(appointment.getVehicle());
        repair.setStatus(RepairStatus.STANDBY);
        repairRepository.save(repair);
    }

    /**
     * Formaliza la entrada física del vehículo en el taller.
     * Solo permite la recepción si existe un presupuesto y está aceptado por el cliente.
     * Registra la fecha de entrada y actualiza la cita a estado {@code EN_TALLER}.
     *
     * @param repairId Identificador único de la reparación.
     * @throws IllegalArgumentException  si no existe ninguna reparación con ese ID.
     * @throws IllegalStateException     si no hay presupuesto aprobado para la reparación.
     */
    @Override
    @Transactional
    public void receiveVehicle(Long repairId) {
        Repair repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        // VALIDACIÓN: Bloqueo  si no hay presupuesto aprobado
        if (repair.getBudget() == null || !Boolean.TRUE.equals(repair.getBudget().getAccepted())) {
            throw new IllegalStateException("No se puede recepcionar el vehículo en el taller hasta que el cliente haya aprobado el presupuesto inicial.");
        }

        // Se registra el día exacto en el que el cliente dejó las llaves
        repair.setEntryDate(LocalDate.now());
        repairRepository.save(repair);

        // La cita pasa a estado EN_TALLER
        Appointment appointment = repair.getAppointment();
        if (appointment != null) {
            appointment.setStatus(org.daw2.tallergo.crud_tallergo.enums.AppointmentStatus.EN_TALLER);
            appointmentRepository.save(appointment);
        }
    }
}