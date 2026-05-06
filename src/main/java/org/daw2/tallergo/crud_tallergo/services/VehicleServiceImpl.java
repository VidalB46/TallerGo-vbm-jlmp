package org.daw2.tallergo.crud_tallergo.services;

import lombok.RequiredArgsConstructor;
import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.daw2.tallergo.crud_tallergo.entities.User;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.VehicleMapper;
import org.daw2.tallergo.crud_tallergo.repositories.UserRepository;
import org.daw2.tallergo.crud_tallergo.repositories.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la lógica de negocio para la gestión de vehículos.
 * Centraliza las validaciones de unicidad (matrícula, VIN) y las búsquedas
 * tanto globales como filtradas por propietario.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    /**
     * Devuelve una página paginada de todos los vehículos del sistema.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de vehículo.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> list(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(VehicleMapper::toDTO);
    }

    /**
     * Devuelve los vehículos paginados de un usuario concreto.
     *
     * @param userId   Identificador del usuario propietario.
     * @param pageable Configuración de paginación.
     * @return Página de DTOs de vehículo filtrados por usuario.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> listByUser(Long userId, Pageable pageable) {
        return vehicleRepository.findByUserId(userId, pageable).map(VehicleMapper::toDTO);
    }

    /**
     * Recupera los datos de un vehículo para rellenar el formulario de edición.
     *
     * @param id Identificador único del vehículo.
     * @return DTO con los campos editables del vehículo.
     * @throws ResourceNotFoundException si no existe ningún vehículo con ese ID.
     */
    @Override
    public VehicleUpdateDTO getForEdit(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", id));
        return VehicleMapper.toUpdateDTO(vehicle);
    }

    /**
     * Crea un nuevo vehículo y lo asocia al usuario indicado.
     *
     * @param dto    DTO con los datos del vehículo a registrar.
     * @param userId Identificador del usuario propietario del vehículo.
     * @throws DuplicateResourceException si ya existe un vehículo con la misma matrícula.
     * @throws ResourceNotFoundException  si no existe ningún usuario con el ID indicado.
     */
    @Override
    public void create(VehicleCreateDTO dto, Long userId) {
        if (dto.getMatricula() != null && vehicleRepository.existsByMatricula(dto.getMatricula())) {
            throw new DuplicateResourceException("vehicle", "matricula", dto.getMatricula());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
        Vehicle vehicle = VehicleMapper.toEntity(dto);
        vehicle.setUser(user);
        vehicleRepository.save(vehicle);
    }

    /**
     * Actualiza los datos de un vehículo existente.
     *
     * @param dto DTO con los nuevos valores, incluyendo el ID del vehículo a actualizar.
     * @throws ResourceNotFoundException si no existe ningún vehículo con ese ID.
     */
    @Override
    public void update(VehicleUpdateDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", dto.getId()));
        VehicleMapper.copyToExistingEntity(dto, vehicle);
        vehicleRepository.save(vehicle);
    }

    /**
     * Elimina un vehículo del sistema por su ID.
     *
     * @param id Identificador único del vehículo.
     * @throws ResourceNotFoundException si no existe ningún vehículo con ese ID.
     */
    @Override
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) throw new ResourceNotFoundException("vehicle", "id", id);
        vehicleRepository.deleteById(id);
    }

    /**
     * Devuelve la vista detallada de un vehículo con su marca cargada.
     *
     * @param id Identificador único del vehículo.
     * @return DTO completo del vehículo con información de marca.
     * @throws ResourceNotFoundException si no existe ningún vehículo con ese ID.
     */
    @Override
    public VehicleDetailDTO getDetail(Long id) {
        Vehicle vehicle = vehicleRepository.findByIdWithBrand(id)
                .orElseThrow(() -> new ResourceNotFoundException("vehicle", "id", id));
        return VehicleMapper.toDetailDTO(vehicle);
    }

    /**
     * Devuelve todos los vehículos de un usuario como lista (sin paginar).
     * Usado principalmente para rellenar selectores en formularios de citas.
     *
     * @param userId Identificador del usuario propietario.
     * @return Lista de DTOs de vehículo del usuario.
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId).stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Búsqueda global de vehículos por modelo, matrícula o VIN (parcial, case-insensitive).
     *
     * @param q        Texto a buscar.
     * @param pageable Configuración de paginación.
     * @return Página de resultados que coincidan con la búsqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> search(String q, Pageable pageable) {
        return vehicleRepository.searchAll(q, pageable).map(VehicleMapper::toDTO);
    }

    /**
     * Búsqueda de vehículos filtrada por propietario.
     *
     * @param q        Texto a buscar en modelo, matrícula o VIN.
     * @param userId   Identificador del usuario propietario.
     * @param pageable Configuración de paginación.
     * @return Página de resultados del usuario que coincidan con la búsqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> searchByUser(String q, Long userId, Pageable pageable) {
        return vehicleRepository.searchByUser(q, userId, pageable).map(VehicleMapper::toDTO);
    }
}