package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA para la tabla 'services'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "workshopServices")
@ToString(exclude = "workshopServices")
@Entity
@Table(name = "services")
public class Service {

    /** INT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** VARCHAR(150) NOT NULL */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** Relación 1:N con WorkshopService */
    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<WorkshopService> workshopServices = new HashSet<>();
}
