package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetailDTO {

    private Long id;
    private String matricula;
    private String model;
    private String color;
    private Integer year;
    private Integer km;

    private BrandDTO brand;
}