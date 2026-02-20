package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.Data;
import java.util.List;

@Data
public class WorkshopDetailDTO {
    private Integer id;
    private String nif;
    private String name;
    private String location;
    private String schedule;
    private List<MechanicDTO> mechanics;
}