package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico de lectura para Brand (listar/detalle).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {
    private Integer id;
    private String name;
    private String country;
}