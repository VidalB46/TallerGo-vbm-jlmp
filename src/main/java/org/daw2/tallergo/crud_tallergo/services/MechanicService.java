package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.MechanicCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.MechanicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MechanicService {
    Page<MechanicDTO> list(Pageable pageable);
    void create(MechanicCreateDTO dto);
    void delete(Long id);
}