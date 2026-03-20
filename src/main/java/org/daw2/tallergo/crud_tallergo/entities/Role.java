package org.daw2.tallergo.crud_tallergo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa los roles de seguridad del sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Identificador único del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre técnico del rol (ej. ROLE_ADMIN, ROLE_USER).
     * Debe ser único y no nulo.
     */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Nombre descriptivo para mostrar en la interfaz de usuario.
     */
    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    /**
     * Descripción opcional sobre los permisos o propósito del rol.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Conjunto de usuarios que tienen asignado este rol.
     * Lado inverso de la relación Many-To-Many.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    /**
     * Constructor para inicializar un rol con sus datos básicos.
     * * @param name Nombre técnico.
     * @param displayName Nombre legible.
     * @param description Descripción del rol.
     */
    public Role(String name, String displayName, String description) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }
}