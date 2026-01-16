package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA para la tabla 'reviews'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    /** BIGINT AUTO_INCREMENT PRIMARY KEY */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** INT CHECK (rating >= 1 AND rating <= 5) */
    @Column(name = "rating")
    private Integer rating;

    /** TEXT NULL - Comentario */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /** Relación N:1 con Workshop */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    /** Relación N:1 con User */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}