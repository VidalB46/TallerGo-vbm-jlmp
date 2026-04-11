package org.daw2.tallergo.crud_tallergo.services;

import org.daw2.tallergo.crud_tallergo.dtos.VehicleCreateDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleDetailDTO;
import org.daw2.tallergo.crud_tallergo.dtos.VehicleUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interfaz de servicio para la gestión del inventario de vehículos.
 */
public interface VehicleService {
    Page<VehicleDTO> list(Pageable pageable);

    /**
     * Recupera los vehículos de un usuario con soporte para paginación.
     * @param userId ID del propietario.
     * @param pageable Configuración de página.
     * @return Página de DTOs.
     */
    Page<VehicleDTO> listByUser(Long userId, Pageable pageable);

    VehicleUpdateDTO getForEdit(Long id);
    void create(VehicleCreateDTO dto);
    void update(VehicleUpdateDTO dto);
    void delete(Long id);
    VehicleDetailDTO getDetail(Long id);
    List<VehicleDTO> getVehiclesByUserId(Long userId);
}