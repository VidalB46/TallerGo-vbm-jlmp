package org.daw2.tallergo.crud_tallergo.daos;

import org.daw2.tallergo.crud_tallergo.entities.Mecanico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class MecanicoDAOImpl implements MecanicoDAO {

    private static final Logger logger = LoggerFactory.getLogger(MecanicoDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    // ───────────────────────────────────────────────────────────────
    // LISTADO
    // ───────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Mecanico> listAll() {
        logger.info("Listando todos los mecánicos");
        return entityManager
                .createQuery("SELECT m FROM Mecanico m", Mecanico.class)
                .getResultList();
    }

    // ───────────────────────────────────────────────────────────────
    // CONSULTAS
    // ───────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Mecanico getById(Long id) {
        logger.info("Buscando mecánico con id {}", id);
        return entityManager.find(Mecanico.class, id);
    }

    // ───────────────────────────────────────────────────────────────
    // INSERCIÓN / ACTUALIZACIÓN / ELIMINACIÓN
    // ───────────────────────────────────────────────────────────────
    @Override
    public void insert(Mecanico mecanico) {
        logger.info("Insertando mecánico {}", mecanico.getNombre());
        entityManager.persist(mecanico);
    }

    @Override
    public void update(Mecanico mecanico) {
        logger.info("Actualizando mecánico con id {}", mecanico.getId());
        entityManager.merge(mecanico);
    }

    @Override
    public void delete(Long id) {
        logger.info("Eliminando mecánico con id {}", id);
        Mecanico mecanico = entityManager.find(Mecanico.class, id);
        if (mecanico != null) {
            entityManager.remove(mecanico);
        }
    }
}