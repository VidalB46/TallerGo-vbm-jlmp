package org.daw2.tallergo.crud_tallergo.daos;

import org.daw2.tallergo.crud_tallergo.entities.Mecanico;

import java.util.List;

/**
 * DAO para la entidad {@link Mecanico}.
 *
 * Define operaciones CRUD básicas.
 */
public interface MecanicoDAO {

    // ───────────────────────────────────────────────────────────────
    // LISTADO
    // ───────────────────────────────────────────────────────────────

    /**
     * Devuelve todos los mecánicos.
     *
     * @return lista de mecánicos.
     */
    List<Mecanico> listAll();


    // ───────────────────────────────────────────────────────────────
    // CONSULTAS
    // ───────────────────────────────────────────────────────────────

    /**
     * Obtiene un mecánico por su id.
     *
     * @param id identificador.
     * @return mecánico o null si no existe.
     */
    Mecanico getById(Long id);


    // ───────────────────────────────────────────────────────────────
    // INSERCIÓN / ACTUALIZACIÓN / ELIMINACIÓN
    // ───────────────────────────────────────────────────────────────

    /**
     * Inserta un nuevo mecánico.
     *
     * @param mecanico mecánico a insertar.
     */
    void insert(Mecanico mecanico);

    /**
     * Actualiza un mecánico existente.
     *
     * @param mecanico mecánico con los datos actualizados.
     */
    void update(Mecanico mecanico);

    /**
     * Elimina un mecánico por id.
     *
     * @param id id del mecánico.
     */
    void delete(Long id);
}