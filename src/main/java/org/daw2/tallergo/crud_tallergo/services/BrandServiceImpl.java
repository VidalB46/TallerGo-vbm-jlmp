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

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Page<BrandDTO> list(Pageable pageable) {
        return brandRepository.findAll(pageable).map(BrandMapper::toDTO);
    }

    @Override
    public List<BrandDTO> listAll() {
        return BrandMapper.toDTOList(brandRepository.findAll());
    }

    @Override
    public BrandUpdateDTO getForEdit(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("brand", "id", id));
        return BrandMapper.toUpdateDTO(brand);
    }

    @Override
    public void create(BrandCreateDTO dto) {
        // No pueden existir dos marcas con el mismo nombre
        if (brandRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("brand", "name", dto.getName());
        }
        Brand brand = BrandMapper.toEntity(dto);
        brandRepository.save(brand);
    }

    @Override
    public void update(BrandUpdateDTO dto) {
        // Comprobar que el nuevo nombre no lo use otra marca distinta
        if (brandRepository.existsByNameAndIdNot(dto.getName(), dto.getId())) {
            throw new DuplicateResourceException("brand", "name", dto.getName());
        }

        Brand brand = brandRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("brand", "id", dto.getId()));

        BrandMapper.copyToExistingEntity(dto, brand);
        brandRepository.save(brand);
    }

    @Override
    public void delete(Integer id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("brand", "id", id);
        }
        brandRepository.deleteById(id);
    }

    @Override
    public BrandDetailDTO getDetail(Integer id) {
        Brand brand = brandRepository.findByIdWithVehicles(id)
                .orElseThrow(() -> new ResourceNotFoundException("brand", "id", id));
        return BrandMapper.toDetailDTO(brand);
    }
}