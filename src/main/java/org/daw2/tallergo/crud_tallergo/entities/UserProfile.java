package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA que almacena la información detallada del perfil de un usuario.
 * Utiliza una relación 1:1 con la tabla 'users' mediante una clave primaria compartida.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_profiles")
public class UserProfile {

    /**
     * Identificador único del perfil.
     * Coincide con el ID del usuario al que pertenece (Shared Primary Key).
     */
    @Id
    @Column(name="user_id")
    private Long id;

    /**
     * Referencia a la entidad de usuario propietaria.
     * La anotación @MapsId indica que el ID de esta entidad se deriva de la entidad User.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Nombre de pila del usuario.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /**
     * Apellidos del usuario.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /**
     * Número de teléfono de contacto.
     */
    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    /**
     * Ruta o URL de la imagen de perfil almacenada.
     */
    @Column(name = "profile_image", length = 255)
    private String profileImage;

    /**
     * Breve descripción o biografía del usuario.
     */
    @Column(name = "bio", length = 500)
    private String bio;

    /**
     * Configuración regional o idioma preferido (ej. 'es_ES').
     */
    @Column(name = "locale", length = 10)
    private String locale;

    /**
     * Fecha y hora de creación del perfil.
     * Gestionado automáticamente por la base de datos.
     */
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del perfil.
     * Gestionado automáticamente por la base de datos.
     */
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    /**
     * Constructor para inicializar el perfil con sus datos básicos.
     * * @param user Entidad User asociada.
     * @param firstName Nombre.
     * @param lastName Apellidos.
     * @param phoneNumber Teléfono.
     * @param profileImage Imagen de perfil.
     * @param bio Biografía.
     * @param locale Idioma.
     */
    public UserProfile(User user, String firstName, String lastName, String phoneNumber, String profileImage, String bio, String locale){
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.bio = bio;
        this.locale = locale;
    }
}