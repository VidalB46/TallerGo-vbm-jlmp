package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDetailDTO;
import org.daw2.tallergo.crud_tallergo.entities.Mechanic;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.MechanicMapper;
import org.daw2.tallergo.crud_tallergo.repositories.MechanicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la lógica de negocio para la gestión de mecánicos.
 * Utiliza {@code @RequiredArgsConstructor} para la inyección de dependencias por constructor.
 */
@Service
@RequiredArgsConstructor
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;

    /**
     * Obtiene una página de mecánicos.
     * Se marca como readOnly para optimizar el rendimiento de la sesión de Hibernate.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de mecánico.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MechanicDTO> list(Pageable pageable) {
        return mechanicRepository.findAll(pageable).map(MechanicMapper::toDTO);
    }

    /**
     * Crea un nuevo mecánico.
     * La transacción asegura que la operación se complete correctamente en la base de datos.
     *
     * @param dto DTO con los datos del mecánico a crear.
     */
    @Override
    @Transactional
    public void create(MechanicCreateDTO dto) {
        // En un escenario real, aquí validaríamos que el WorkshopId del DTO existe
        Mechanic mechanic = MechanicMapper.toEntity(dto);
        mechanicRepository.save(mechanic);
    }

    /**
     * Elimina un mecánico por su ID.
     * Lanza una excepción personalizada si el mecánico no existe para informar al usuario.
     *
     * @param id Identificador único del mecánico a eliminar.
     * @throws ResourceNotFoundException si no existe ningún mecánico con ese ID.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!mechanicRepository.existsById(id)) {
            throw new ResourceNotFoundException("mechanic", "id", id);
        }
        mechanicRepository.deleteById(id);
    }

    /**
     * Obtiene el detalle completo de un mecánico con su taller asociado.
     *
     * @param id Identificador único del mecánico.
     * @return DTO de detalle con la información del taller cargada.
     * @throws ResourceNotFoundException si no existe ningún mecánico con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public MechanicDetailDTO getDetail(Long id) {
        Mechanic mechanic = mechanicRepository.findByIdWithWorkshop(id)
                .orElseThrow(() -> new ResourceNotFoundException("mechanic", "id", id));
        return MechanicMapper.toDetailDTO(mechanic);
    }

    /**
     * Búsqueda de mecánicos por nombre o especialidad (parcial, case-insensitive).
     *
     * @param q        Texto a buscar en los campos del mecánico.
     * @param pageable Configuración de paginación.
     * @return Página de resultados que coincidan con la búsqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MechanicDTO> search(String q, Pageable pageable) {
        return mechanicRepository.search(q, pageable).map(MechanicMapper::toDTO);
    }
}