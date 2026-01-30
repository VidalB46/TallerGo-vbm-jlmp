package org.daw2.tallergo.crud_tallergo.daos;

import org.daw2.tallergo.crud_tallergo.entities.Taller;
import java.util.List;

/**
 * DAO para la entidad {@link Taller}.
 *
 * Define operaciones CRUD básicas.
 */
public interface TallerDAO {

    // ───────────────────────────────────────────────────────────────
    // LISTADO
    // ───────────────────────────────────────────────────────────────

    /**
     * Devuelve todos los talleres.
     *
     * @return lista de talleres.
     */
    List<Taller> listAll();


    // ───────────────────────────────────────────────────────────────
    // CONSULTAS
    // ───────────────────────────────────────────────────────────────

    /**
     * Obtiene un taller por su id.
     *
     * @param id identificador.
     * @return taller o null si no existe.
     */
    Taller getById(Long id);


    // ───────────────────────────────────────────────────────────────
    // INSERCIÓN / ACTUALIZACIÓN / ELIMINACIÓN
    // ───────────────────────────────────────────────────────────────

    /**
     * Inserta un nuevo taller.
     *
     * @param taller taller a insertar.
     */
    void insert(Taller taller);

    /**
     * Actualiza un taller existente.
     *
     * @param taller taller con los datos actualizados.
     */
    void update(Taller taller);

    /**
     * Elimina un taller por id.
     *
     * @param id id del taller.
     */
    void delete(Long id);
}