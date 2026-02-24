package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.VehicleCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.VehicleMapper;
import org.daw2.tallergo.crud_tallergo.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Page<VehicleDTO> list(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(VehicleMapper::toDTO);
    }

    @Override
    public VehicleUpdateDTO getForEdit(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", id));
        return VehicleMapper.toUpdateDTO(vehicle);
    }

    @Override
    public void create(VehicleCreateDTO dto) {
        // Validar que la matrícula no exista ya
        if (dto.getMatricula() != null && vehicleRepository.existsByMatricula(dto.getMatricula())) {
            throw new DuplicateResourceException("vehicle", "matricula", dto.getMatricula());
        }

        Vehicle vehicle = VehicleMapper.toEntity(dto);

        vehicleRepository.save(vehicle);
    }

    @Override
    public void update(VehicleUpdateDTO dto) {
        // Validar que la nueva matrícula no la tenga ya otro vehículo
        if (dto.getMatricula() != null && vehicleRepository.existsByMatriculaAndIdNot(dto.getMatricula(), dto.getId())) {
            throw new DuplicateResourceException("vehicle", "matricula", dto.getMatricula());
        }

        Vehicle vehicle = vehicleRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", dto.getId()));

        VehicleMapper.copyToExistingEntity(dto, vehicle);
        vehicleRepository.save(vehicle);
    }

    @Override
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("vehicle", "id", id);
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public VehicleDetailDTO getDetail(Long id) {
        Vehicle vehicle = vehicleRepository.findByIdWithBrand(id)
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", id));
        return VehicleMapper.toDetailDTO(vehicle);
    }
}