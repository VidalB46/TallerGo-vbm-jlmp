package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.BrandCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.BrandUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {

    Page<BrandDTO> list(Pageable pageable);

    List<BrandDTO> listAll(); // Útil para rellenar los "select" desplegables en los formularios

    BrandUpdateDTO getForEdit(Integer id);

    void create(BrandCreateDTO dto);

    void update(BrandUpdateDTO dto);

    void delete(Integer id);

    BrandDetailDTO getDetail(Integer id);
}