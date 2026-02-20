package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MechanicDetailDTO {
    private Long id;
    private String name;
    private String specialty;
    private WorkshopDTO workshop;
}