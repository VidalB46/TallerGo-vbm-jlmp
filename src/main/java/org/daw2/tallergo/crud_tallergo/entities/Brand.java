package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA para la tabla 'brands'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "vehicles")
@ToString(exclude = "vehicles")
@Entity
@Table(name = "brands")
public class Brand {

    /** INT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** VARCHAR(100) NOT NULL UNIQUE */
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /** VARCHAR(100) NULL */
    @Column(name = "country", length = 100)
    private String country;

    /** Relación 1:N con Vehicle */
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<Vehicle> vehicles = new HashSet<>();

    public Brand(String name, String country) {
        this.name = name;
        this.country = country;
    }
}