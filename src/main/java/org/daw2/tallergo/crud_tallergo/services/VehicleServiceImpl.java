package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.VehicleMapper;
import org.daw2.tallergo.crud_tallergo.repositories.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> list(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(VehicleMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> listByUser(Long userId, Pageable pageable) {
        return vehicleRepository.findByUserId(userId, pageable).map(VehicleMapper::toDTO);
    }

    @Override
    public VehicleUpdateDTO getForEdit(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", id));
        return VehicleMapper.toUpdateDTO(vehicle);
    }

    @Override
    public void create(VehicleCreateDTO dto) {
        if (dto.getMatricula() != null && vehicleRepository.existsByMatricula(dto.getMatricula())) {
            throw new DuplicateResourceException("vehicle", "matricula", dto.getMatricula());
        }
        vehicleRepository.save(VehicleMapper.toEntity(dto));
    }

    @Override
    public void update(VehicleUpdateDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", dto.getId()));
        VehicleMapper.copyToExistingEntity(dto, vehicle);
        vehicleRepository.save(vehicle);
    }

    @Override
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) throw new ResourceNotFoundException("vehicle", "id", id);
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleDetailDTO getDetail(Long id) {
        Vehicle vehicle = vehicleRepository.findByIdWithBrand(id)
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", id));
        return VehicleMapper.toDetailDTO(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId).stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }
}