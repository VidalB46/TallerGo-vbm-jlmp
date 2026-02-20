package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.VehicleCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleService {

    Page<VehicleDTO> list(Pageable pageable);

    VehicleUpdateDTO getForEdit(Long id);

    void create(VehicleCreateDTO dto);

    void update(VehicleUpdateDTO dto);

    void delete(Long id);

    VehicleDetailDTO getDetail(Long id);
}