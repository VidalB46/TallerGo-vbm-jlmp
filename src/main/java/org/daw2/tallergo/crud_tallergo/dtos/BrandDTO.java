package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) simple que representa una Marca de vehículos.
 * <p>
 * A diferencia de BrandDetailDTO, este objeto solo contiene la información básica
 * de la marca, sin incluir relaciones complejas como la lista de modelos.
 * Es ideal para listados ligeros, selectores (combobox) o respuestas rápidas de la API.
 * </p>
 * <p>
 * Incluye la anotación {@code @Builder} de Lombok para facilitar la creación
 * e instanciación del objeto utilizando el patrón Builder.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {

    /**
     * Identificador único de la marca.
     */
    private Integer id;

    /**
     * Nombre de la marca de vehículos (ej. "Toyota", "Peugeot").
     */
    private String name;

    /**
     * País de origen o sede principal de la marca.
     */
    private String country;
}