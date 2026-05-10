package org.daw2.tallergo.crud_tallergo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ligero para mostrar reseñas en listados o carruseles.
 * Incluye información mínima de la reseña y del autor necesaria
 * para renderizar acciones condicionales en la vista.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    /**
     * Identificador único de la reseña.
     */
    private Long id;

    /**
     * Puntuación otorgada por el usuario.
     */
    private Integer rating;

    /**
     * Comentario escrito por el usuario.
     */
    private String comment;

    /**
     * Nombre completo del cliente que ha dejado la reseña.
     */
    private String userFullName;

    /**
     * Email del usuario autor de la reseña.
     * Se utiliza para comprobar en la vista si el usuario autenticado
     * es el propietario de la reseña y, por tanto, puede eliminarla.
     */
    private String userEmail;

    /**
     * Nombre del taller valorado.
     */
    private String workshopName;
}