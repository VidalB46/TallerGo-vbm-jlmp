package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.RepairCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.RepairUpdateDTO;
import org.daw2.tallergo.crud_tallergo.enums.RepairStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepairService {
    RepairDetailDTO getRepairById(Long id);
    Page<RepairDTO> getAllRepairs(Pageable pageable);
    RepairDTO createRepair(RepairCreateDTO dto);
    RepairDTO updateRepair(RepairUpdateDTO dto);
    void updateStatus(Long id, RepairStatus newStatus);
    void deliverVehicle(Long id);
}