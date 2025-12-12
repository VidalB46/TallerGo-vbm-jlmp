package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandUpdateDTO {

    @NotNull(message = "{msg.brand.id.notNull}")
    private Integer id;

    @NotBlank(message = "{msg.brand.name.notEmpty}")
    @Size(max = 100, message = "{msg.brand.name.size}")
    private String name;

    @Size(max = 100, message = "{msg.brand.country.size}")
    private String country;
}