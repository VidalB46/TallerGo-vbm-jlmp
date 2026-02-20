package org.daw2.tallergo.crud_tallergo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WorkshopCreateDTO {
    @NotBlank(message = "{msg.workshop.nif.notBlank}")
    @Size(max = 20)
    private String nif;

    @NotBlank(message = "{msg.workshop.name.notBlank}")
    @Size(max = 150)
    private String name;

    private String phone;
    private String location;
    private String email;
    private String schedule;
}