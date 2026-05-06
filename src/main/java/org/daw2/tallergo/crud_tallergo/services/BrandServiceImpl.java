package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.BrandCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandUpdateDTO;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.exceptions.DuplicateResourceException;
import org.daw2.tallergo.crud_tallergo.exceptions.ResourceNotFoundException;
import org.daw2.tallergo.crud_tallergo.mappers.BrandMapper;
import org.daw2.tallergo.crud_tallergo.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de la lógica de negocio para la gestión de marcas.
 * Utiliza @Transactional a nivel de clase para asegurar la integridad de las operaciones en DB.
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    /**
     * Devuelve una página paginada con todas las marcas del sistema.
     *
     * @param pageable Configuración de página, tamaño y ordenamiento.
     * @return Página de DTOs de marca.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BrandDTO> list(Pageable pageable) {
        return brandRepository.findAll(pageable).map(BrandMapper::toDTO);
    }

    /**
     * Devuelve la lista completa de marcas sin paginación.
     * Usado principalmente para poblar selectores en formularios de vehículos.
     *
     * @return Lista de todos los DTOs de marca.
     */
    @Override
    @Transactional(readOnly = true)
    public List<BrandDTO> listAll() {
        return BrandMapper.toDTOList(brandRepository.findAll());
    }

    /**
     * Recupera los datos de una marca para rellenar el formulario de edición.
     *
     * @param id Identificador único de la marca.
     * @return DTO con los campos editables de la marca.
     * @throws ResourceNotFoundException si no existe ninguna marca con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public BrandUpdateDTO getForEdit(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("brand", "id", id));
        return BrandMapper.toUpdateDTO(brand);
    }

    /**
     * Crea una nueva marca validando que el nombre no esté ya registrado.
     *
     * @param dto DTO con los datos de la marca a crear.
     * @throws DuplicateResourceException si ya existe una marca con el mismo nombre.
     */
    @Override
    public void create(BrandCreateDTO dto) {
        // Validación de negocio: No se permiten nombres duplicados
        if (brandRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("brand", "name", dto.getName());
        }

        Brand brand = BrandMapper.toEntity(dto);
        brandRepository.save(brand);
    }

    /**
     * Actualiza los datos de una marca existente.
     *
     * @param dto DTO con los nuevos valores, incluyendo el ID de la marca a actualizar.
     * @throws DuplicateResourceException si el nuevo nombre ya pertenece a otra marca distinta.
     * @throws ResourceNotFoundException  si no existe ninguna marca con el ID indicado.
     */
    @Override
    public void update(BrandUpdateDTO dto) {
        // Validación de negocio: El nombre no puede colisionar con otra marca (distinta a la actual)
        if (brandRepository.existsByNameAndIdNot(dto.getName(), dto.getId())) {
            throw new DuplicateResourceException("brand", "name", dto.getName());
        }

        Brand brand = brandRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("brand", "id", dto.getId()));

        // Actualizamos el estado de la entidad gestionada por JPA
        BrandMapper.copyToExistingEntity(dto, brand);
        brandRepository.save(brand);
    }

    /**
     * Elimina una marca del sistema por su ID.
     *
     * @param id Identificador único de la marca.
     * @throws ResourceNotFoundException si no existe ninguna marca con ese ID.
     */
    @Override
    public void delete(Integer id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("brand", "id", id);
        }
        brandRepository.deleteById(id);
    }

    /**
     * Devuelve el detalle completo de una marca, incluyendo los vehículos asociados.
     *
     * @param id Identificador único de la marca.
     * @return DTO de detalle con la lista de vehículos cargada.
     * @throws ResourceNotFoundException si no existe ninguna marca con ese ID.
     */
    @Override
    @Transactional(readOnly = true)
    public BrandDetailDTO getDetail(Integer id) {
        Brand brand = brandRepository.findByIdWithVehicles(id)
                .orElseThrow(() -> new ResourceNotFoundException("brand", "id", id));
        return BrandMapper.toDetailDTO(brand);
    }

    /**
     * Búsqueda de marcas por nombre y/o país de origen con paginación.
     * Si ningún filtro está activo, devuelve todas las marcas.
     *
     * @param q        Texto a buscar en el nombre de la marca (puede ser nulo o vacío).
     * @param country  País de origen por el que filtrar (puede ser nulo o vacío).
     * @param pageable Configuración de paginación.
     * @return Página de marcas que coincidan con los criterios de búsqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BrandDTO> search(String q, String country, Pageable pageable) {
        boolean hasQ = q != null && !q.isBlank();
        boolean hasCountry = country != null && !country.isBlank();

        if (hasQ && hasCountry) {
            return brandRepository.searchByNameAndCountry(q.trim(), country.trim(), pageable).map(BrandMapper::toDTO);
        } else if (hasQ) {
            return brandRepository.searchByName(q.trim(), pageable).map(BrandMapper::toDTO);
        } else if (hasCountry) {
            return brandRepository.filterByCountry(country.trim(), pageable).map(BrandMapper::toDTO);
        } else {
            return brandRepository.findAll(pageable).map(BrandMapper::toDTO);
        }
    }

    /**
     * Devuelve la lista de países de origen distintos registrados en el sistema.
     * Usado para poblar el selector de filtro por país en la vista de marcas.
     *
     * @return Lista de nombres de países únicos ordenados alfabéticamente.
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getDistinctCountries() {
        return brandRepository.findDistinctCountries();
    }
}