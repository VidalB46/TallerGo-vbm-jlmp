package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase `Brand` representa una entidad que modela una marca de vehículos dentro de la base de datos.
 * Contiene tres campos: `id`, `name` y `country`.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "country", length = 100)
    private String country;

    @OneToMany(
            mappedBy = "brand",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = false
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Vehicle> vehicles = new ArrayList<>();

    public Brand(String name, String country) {
        this.name = name;
        this.country = country;
    }
}
