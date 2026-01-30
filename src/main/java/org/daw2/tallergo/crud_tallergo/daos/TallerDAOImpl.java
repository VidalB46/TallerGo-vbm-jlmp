package org.daw2.tallergo.crud_tallergo.daos;

import org.daw2.tallergo.crud_tallergo.entities.Taller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class TallerDAOImpl implements TallerDAO {

    private static final Logger logger = LoggerFactory.getLogger(TallerDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    // ───────────────────────────────────────────────────────────────
    // LISTADO
    // ───────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Taller> listAll() {
        logger.info("Listando todos los talleres");
        return entityManager
                .createQuery("SELECT t FROM Taller t", Taller.class)
                .getResultList();
    }

    // ───────────────────────────────────────────────────────────────
    // CONSULTAS
    // ───────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Taller getById(Long id) {
        logger.info("Buscando taller con id {}", id);
        return entityManager.find(Taller.class, id);
    }

    // ───────────────────────────────────────────────────────────────
    // INSERCIÓN / ACTUALIZACIÓN / ELIMINACIÓN
    // ───────────────────────────────────────────────────────────────
    @Override
    public void insert(Taller taller) {
        logger.info("Insertando taller {}", taller.getNombre());
        entityManager.persist(taller);
    }

    @Override
    public void update(Taller taller) {
        logger.info("Actualizando taller con id {}", taller.getId());
        entityManager.merge(taller);
    }

    @Override
    public void delete(Long id) {
        logger.info("Eliminando taller con id {}", id);
        Taller taller = entityManager.find(Taller.class, id);
        if (taller != null) {
            entityManager.remove(taller);
        }
    }
}