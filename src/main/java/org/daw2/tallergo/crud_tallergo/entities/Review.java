package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa una reseña o valoración de un taller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    /**
     * Identificador único de la reseña.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Calificación numérica otorgada (típicamente entre 1 y 5).
     */
    @Column(name = "rating")
    private Integer rating;

    /**
     * Comentario textual u opinión del usuario sobre el taller.
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /**
     * Taller al que se dirige la reseña.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    /**
     * Usuario que ha escrito la reseña.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}