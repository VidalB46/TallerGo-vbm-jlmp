package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
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
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MechanicDTO> list(Pageable pageable) {
        return mechanicRepository.findAll(pageable).map(MechanicMapper::toDTO);
    }

    /**
     * Crea un nuevo mecánico.
     * La transacción asegura que la operación se complete correctamente en la base de datos.
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
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (!mechanicRepository.existsById(id)) {
            throw new ResourceNotFoundException("mechanic", "id", id);
        }
        mechanicRepository.deleteById(id);
    }
}