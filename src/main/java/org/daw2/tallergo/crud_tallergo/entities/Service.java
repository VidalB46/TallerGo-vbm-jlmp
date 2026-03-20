package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un tipo de servicio o actividad técnica ofrecida.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "workshopServices")
@ToString(exclude = "workshopServices")
@Entity
@Table(name = "services")
public class Service {

    /**
     * Identificador único del servicio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre descriptivo del servicio (ej. Cambio de aceite, Equilibrado).
     */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /**
     * Conjunto de relaciones que vinculan este servicio con los diferentes talleres.
     */
    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<WorkshopService> workshopServices = new HashSet<>();
}