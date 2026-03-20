package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) que representa los detalles completos de una Marca de vehículos.
 * <p>
 * A diferencia de un DTO simple, esta clase incluye no solo la información básica
 * de la marca, sino también la lista de vehículos (modelos) asociados a ella.
 * Se utiliza principalmente para enviar la información estructurada a la vista o al cliente API.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDetailDTO {

    /**
     * Identificador único de la marca.
     */
    private Integer id;

    /**
     * Nombre de la marca de vehículos (por ejemplo: "Renault", "Ford", "Seat").
     */
    private String name;

    /**
     * País de origen o sede principal de la marca.
     */
    private String country;

    /**
     * Lista de vehículos asociados a esta marca.
     * Permite mostrar qué modelos específicos de TallerGO pertenecen a esta marca en concreto.
     */
    private List<VehicleDTO> vehicles;
}