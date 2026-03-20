package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa una marca de vehículos en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "vehicles")
@ToString(exclude = "vehicles")
@Entity
@Table(name = "brands")
public class Brand {

    /**
     * Identificador único de la marca.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de la marca. Debe ser único y no nulo.
     */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /**
     * País de origen de la marca.
     */
    @Column(name = "country", length = 100)
    private String country;

    /**
     * Conjunto de vehículos asociados a esta marca.
     */
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<Vehicle> vehicles = new HashSet<>();

    /**
     * Constructor para inicializar una marca con nombre y país.
     * * @param name Nombre de la marca.
     * @param country País de procedencia.
     */
    public Brand(String name, String country) {
        this.name = name;
        this.country = country;
    }
}