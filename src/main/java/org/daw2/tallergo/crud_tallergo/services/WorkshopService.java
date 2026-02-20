package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface WorkshopService {
    Page<WorkshopDTO> list(Pageable pageable);
    List<WorkshopDTO> listAll();
    void create(WorkshopCreateDTO dto);
    WorkshopDetailDTO getDetail(Integer id);
    void delete(Integer id);
}