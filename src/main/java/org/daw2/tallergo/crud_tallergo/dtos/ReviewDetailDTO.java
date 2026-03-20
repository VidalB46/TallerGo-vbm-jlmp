package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para visualizar los detalles completos de una reseña.
 * Útil para paneles de administración (moderación de comentarios) o vistas
 * en profundidad donde se requiere el contexto completo del usuario y el taller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailDTO {

    private Long id;

    /**
     * Puntuación otorgada por el usuario (1-5).
     */
    private Integer rating;

    /**
     * Comentario o feedback detallado.
     */
    private String comment;

    // --- Relaciones detalladas ---

    /**
     * Datos del taller al que pertenece la valoración.
     * Usamos el DTO para poder mostrar su nombre, NIF, etc.
     */
    private WorkshopDTO workshop;

    /**
     * Nombre completo del usuario que escribió la reseña.
     * (Se extrae del UserProfile en el Mapper).
     */
    private String userFullName;

    /**
     * Email del usuario, vital para que el administrador pueda contactarle
     * si hay alguna queja en la reseña.
     */
    private String userEmail;
}