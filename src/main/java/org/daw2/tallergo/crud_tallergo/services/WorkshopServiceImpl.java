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

/**
 * Implementación de la lógica de negocio para la gestión de talleres.
 * Utiliza {@code @RequiredArgsConstructor} para la inyección de dependencias por constructor.
 */
@Service
@RequiredArgsConstructor
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopDTO> list(Pageable pageable) {
        return workshopRepository.findAll(pageable).map(WorkshopMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopDTO> listAll() {
        return WorkshopMapper.toDTOList(workshopRepository.findAll());
    }

    @Override
    @Transactional
    public void create(WorkshopCreateDTO dto) {
        // Validación de NIF duplicado (regla de negocio crítica)
        if (workshopRepository.existsByNif(dto.getNif())) {
            throw new DuplicateResourceException("workshop", "nif", dto.getNif());
        }
        workshopRepository.save(WorkshopMapper.toEntity(dto));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkshopDetailDTO getDetail(Integer id) {
        // Cargamos el taller junto con sus servicios para evitar LazyInitializationException
        Workshop workshop = workshopRepository.findByIdWithServices(id)
                .orElseThrow(() -> new ResourceNotFoundException("workshop", "id", id));
        return WorkshopMapper.toDetailDTO(workshop);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!workshopRepository.existsById(id)) {
            throw new ResourceNotFoundException("workshop", "id", id);
        }
        workshopRepository.deleteById(id);
    }
}