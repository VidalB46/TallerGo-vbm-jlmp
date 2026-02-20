package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.Workshop;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.WorkshopMapper;
import org.daw2.tallergo.crud_tallergo.repositories.WorkshopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;

    @Override
    public Page<WorkshopDTO> list(Pageable pageable) {
        return workshopRepository.findAll(pageable).map(WorkshopMapper::toDTO);
    }

    @Override
    public List<WorkshopDTO> listAll() {
        return WorkshopMapper.toDTOList(workshopRepository.findAll());
    }

    @Override
    @Transactional
    public void create(WorkshopCreateDTO dto) {
        if (workshopRepository.existsByNif(dto.getNif())) {
            throw new DuplicateResourceException("workshop", "nif", dto.getNif());
        }
        workshopRepository.save(WorkshopMapper.toEntity(dto));
    }

    @Override
    public WorkshopDetailDTO getDetail(Integer id) {
        Workshop workshop = workshopRepository.findByIdWithServices(id)
                .orElseThrow(() -> new ResourceNotFoundException("workshop", "id", id));
        return WorkshopMapper.toDetailDTO(workshop);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        workshopRepository.deleteById(id);
    }
}