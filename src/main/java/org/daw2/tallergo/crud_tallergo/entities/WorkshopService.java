package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidad JPA que actúa como tabla intermedia entre talleres y servicios.
 * Permite definir atributos específicos como el precio y la duración para cada
 * combinación de taller y servicio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workshop_services", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workshop_id", "service_id"})
})
public class WorkshopService {

    /**
     * Identificador único de la relación taller-servicio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Taller que ofrece el servicio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    /**
     * Tipo de servicio ofrecido por el taller.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    /**
     * Precio establecido por este taller concreto para este servicio.
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Duración estimada del servicio expresada en minutos.
     */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     * Constructor para inicializar una relación de servicio en un taller.
     * * @param workshop Taller propietario.
     * @param service Servicio ofrecido.
     * @param price Coste del servicio.
     * @param durationMinutes Tiempo estimado de ejecución.
     */
    public WorkshopService(Workshop workshop, Service service, BigDecimal price, Integer durationMinutes) {
        this.workshop = workshop;
        this.service = service;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }
}