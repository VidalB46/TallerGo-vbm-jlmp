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

    /**
     * Devuelve una página paginada con todos los talleres del sistema.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de taller.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopDTO> list(Pageable pageable) {
        return workshopRepository.findAll(pageable).map(WorkshopMapper::toDTO);
    }

    /**
     * Devuelve la lista completa de talleres sin paginación.
     * Usado para poblar selectores y desplegables en el frontend.
     *
     * @return Lista de DTOs de todos los talleres.
     */
    @Override
    @Transactional(readOnly = true)
    public List<WorkshopDTO> listAll() {
        return WorkshopMapper.toDTOList(workshopRepository.findAll());
    }

    /**
     * Crea un nuevo taller validando que el NIF no esté ya registrado.
     *
     * @param dto DTO con los datos del taller a crear.
     * @throws DuplicateResourceException si ya existe un taller con el mismo NIF.
     */
    @Override
    @Transactional
    public void create(WorkshopCreateDTO dto) {
        // Validación de NIF duplicado (regla de negocio crítica)
        if (workshopRepository.existsByNif(dto.getNif())) {
            throw new DuplicateResourceException("workshop", "nif", dto.getNif());
        }
        workshopRepository.save(WorkshopMapper.toEntity(dto));
    }

    /**
     * Devuelve el detalle completo de un taller, incluyendo la lista de mecánicos.
     *
     * @param id Identificador único del taller.
     * @return DTO de detalle del taller con sus mecánicos cargados.
     * @throws ResourceNotFoundException si no existe ningún taller con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public WorkshopDetailDTO getDetail(Integer id) {
        // Cargamos el taller junto con sus mecánicos para evitar LazyInitializationException
        Workshop workshop = workshopRepository.findByIdWithMechanics(id)
                .orElseThrow(() -> new ResourceNotFoundException("workshop", "id", id));
        return WorkshopMapper.toDetailDTO(workshop);
    }

    /**
     * Elimina un taller y limpia sus relaciones antes de borrar el registro.
     *
     * @param id Identificador único del taller a eliminar.
     * @throws ResourceNotFoundException si no existe ningún taller con ese ID.
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("workshop", "id", id));

        // Limpiar relaciones — cascade ALL borrará lo que corresponda
        workshop.getWorkshopServices().clear();
        workshop.getReviews().clear();
        workshop.getMechanics().clear();
        workshop.getAppointments().clear();

        workshopRepository.saveAndFlush(workshop);
        workshopRepository.deleteById(id);
    }

    /**
     * Recupera los datos de un taller para rellenar el formulario de edición.
     *
     * @param id Identificador único del taller.
     * @return DTO con los campos editables del taller.
     * @throws ResourceNotFoundException si no existe ningún taller con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public WorkshopUpdateDTO getForEdit(Integer id) {
        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("workshop", "id", id));
        WorkshopUpdateDTO dto = new WorkshopUpdateDTO();
        dto.setId(workshop.getId());
        dto.setNif(workshop.getNif());
        dto.setName(workshop.getName());
        dto.setPhone(workshop.getPhone());
        dto.setLocation(workshop.getLocation());
        dto.setEmail(workshop.getEmail());
        dto.setSchedule(workshop.getSchedule());
        return dto;
    }

    /**
     * Actualiza los datos de un taller existente.
     *
     * @param dto DTO con los nuevos valores, incluyendo el ID del taller a actualizar.
     * @throws ResourceNotFoundException si no existe ningún taller con ese ID.
     */
    @Override
    @Transactional
    public void update(WorkshopUpdateDTO dto) {
        Workshop workshop = workshopRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("workshop", "id", dto.getId()));
        WorkshopMapper.copyToExistingEntity(dto, workshop);
        workshopRepository.save(workshop);
    }

    /**
     * Busca talleres cuyo nombre contenga el texto indicado (case-insensitive).
     *
     * @param name     Texto a buscar en el nombre del taller.
     * @param pageable Configuración de paginación.
     * @return Página de talleres que coincidan con el criterio.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopDTO> search(String name, Pageable pageable) {
        return workshopRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(WorkshopMapper::toDTO);
    }
}