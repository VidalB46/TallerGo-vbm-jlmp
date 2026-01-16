package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entidad JPA para la tabla intermedia 'workshop_services'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workshop_services", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workshop_id", "service_id"}) // Para que no se repita la pareja
})
public class WorkshopService {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** * Relación N:1 con Workshop.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    /** * Relación N:1 con Service.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    /** DECIMAL(10, 2) NOT NULL - Precio del servicio */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** INT NULL - Duración en minutos */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /** Constructor  */
    public WorkshopService(Workshop workshop, Service service, BigDecimal price, Integer durationMinutes) {
        this.workshop = workshop;
        this.service = service;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }
}