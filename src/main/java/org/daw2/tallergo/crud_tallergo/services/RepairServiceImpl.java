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
 */
@Service
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public RepairDetailDTO getRepairById(Long id) {
        Repair repair = repairRepository.findByIdWithVehicleAndAppointment(id)
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));
        return RepairMapper.toDetailDTO(repair);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RepairDTO> getAllRepairs(Pageable pageable) {
        return repairRepository.findAll(pageable).map(RepairMapper::toDTO);
    }

    @Override
    @Transactional
    public RepairDTO updateRepair(RepairUpdateDTO dto) {
        Repair repair = repairRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));
        RepairMapper.updateEntity(dto, repair);
        return RepairMapper.toDTO(repairRepository.save(repair));
    }

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