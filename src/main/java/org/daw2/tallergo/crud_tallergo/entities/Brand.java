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
@Entity // Marca esta clase como una entidad gestionada por JPA
@Table(name = "brands") // Especifica el nombre de la tabla asociada a esta entidad
public class Brand {

    // Identificador único de la marca.
    // En el schema.sql definiste 'id INT', por lo que usamos Integer.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nombre de la marca (VARCHAR(100) NOT NULL UNIQUE).
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    // País de origen de la marca (VARCHAR(100)).
    @Column(name = "country", length = 100)
    private String country;

    /**
     * Lista de vehículos pertenecientes a la marca.
     * - LAZY: no se cargan hasta que se accede a 'vehicles'
     * - mappedBy: el dueño de la relación es Vehicle.brand
     * - Con cascade ALL: así se borrarían los vehículos asociados si se elimina la marca
     */
    @OneToMany(
            mappedBy = "brand",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = false
    )
    @ToString.Exclude // Evita bucles infinitos en el toString
    @EqualsAndHashCode.Exclude // Evita bucles en comparaciones equals/hashCode
    private List<Vehicle> vehicles = new ArrayList<>();

    /**
     * Constructor personalizado sin ID y sin la lista de vehículos.
     * Útil para crear instancias antes de insertar en BBDD.
     *
     * @param name Nombre de la marca.
     * @param country País de la marca.
     */
    public Brand(String name, String country) {
        this.name = name;
        this.country = country;
    }
}
