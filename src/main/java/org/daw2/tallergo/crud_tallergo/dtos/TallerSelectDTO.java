package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ligero para selects de Taller
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TallerSelectDTO {

    private Long id;
    private String nombre;
}