package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDetailDTO {
    private Integer id;
    private String name;
    private String country;

    private List<VehicleDTO> vehicles;
}
