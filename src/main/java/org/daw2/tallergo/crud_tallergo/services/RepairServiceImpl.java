package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.RepairCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Appointment;
import org.daw2.tallergo.crud_tallergo.entities.Repair;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.mappers.RepairMapper;
import org.daw2.tallergo.crud_tallergo.repositories.AppointmentRepository;
import org.daw2.tallergo.crud_tallergo.repositories.RepairRepository;
import org.daw2.tallergo.crud_tallergo.repositories.VehicleRepository;
import org.daw2.tallergo.crud_tallergo.services.RepairService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepairServiceImpl implements RepairService {

    private final RepairRepository repairRepository;
    private final AppointmentRepository appointmentRepository;
    private final VehicleRepository vehicleRepository;

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
    public RepairDTO createRepair(RepairCreateDTO dto) {
        // Validar que la cita existe y no tiene ya una reparación
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        if (repairRepository.existsByAppointmentId(appointment.getId())) {
            throw new IllegalStateException("Esta cita ya tiene una reparación asociada");
        }

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

        Repair repair = RepairMapper.toEntity(dto, appointment, vehicle);
        return RepairMapper.toDTO(repairRepository.save(repair));
    }

    @Override
    @Transactional
    public RepairDTO updateRepair(RepairUpdateDTO dto) {
        Repair repair = repairRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reparación no encontrada"));

        RepairMapper.updateEntity(dto, repair);
        return RepairMapper.toDTO(repairRepository.save(repair));
    }
}