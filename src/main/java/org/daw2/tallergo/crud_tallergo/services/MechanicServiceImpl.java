package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
import org.daw2.tallergo.crud_tallergo.entities.Mechanic;
import org.daw2.tallergo.crud_tallergo.mappers.MechanicMapper;
import org.daw2.tallergo.crud_tallergo.repositories.MechanicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;

    @Override
    public Page<MechanicDTO> list(Pageable pageable) {
        return mechanicRepository.findAll(pageable).map(MechanicMapper::toDTO);
    }

    @Override
    @Transactional
    public void create(MechanicCreateDTO dto) {
        Mechanic mechanic = MechanicMapper.toEntity(dto);
        mechanicRepository.save(mechanic);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        mechanicRepository.deleteById(id);
    }
}