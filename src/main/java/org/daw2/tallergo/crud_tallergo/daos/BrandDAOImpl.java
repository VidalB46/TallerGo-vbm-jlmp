package org.daw2.tallergo.crud_tallergo.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class BrandDAOImpl implements BrandDAO {

    private static final Logger logger = LoggerFactory.getLogger(BrandDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Brand> listAllBrands() {
        logger.info("Entrando en el metodo listAllBrands");
        String hql = "SELECT b FROM Brand b";
        List<Brand> brands = entityManager.createQuery(hql, Brand.class).getResultList();
        logger.info("Retrieved {} brands from the database", brands.size());
        return brands;
    }

    @Override
    public void insertBrand(Brand brand) {
        logger.info("Inserting brand with name: {}", brand.getName());
        entityManager.persist(brand);
        logger.info("Inserted brand successful");
    }

    @Override
    public void updateBrand(Brand brand) {
        logger.info("Updating brand with id: {}", brand.getId());
        entityManager.merge(brand);
        logger.info("Updated brand with id: {}", brand.getId());
    }

    @Override
    public void deleteBrand(Integer id) {
        logger.info("Deleting brand with id: {}", id);
        Brand brand = entityManager.find(Brand.class, id);
        if (brand != null) {
            entityManager.remove(brand);
            logger.info("Deleted brand with id: {}", id);
        } else {
            logger.warn("Brand with id: {} not found.", id);
        }
    }

    @Override
    public Brand getBrandById(Integer id) {
        logger.info("Retrieving brand by id: {}", id);
        Brand brand = entityManager.find(Brand.class, id);
        if (brand != null) {
            logger.info("Brand retrieved: {} - {}", brand.getId(), brand.getName());
        } else {
            logger.warn("No brand found with id: {}", id);
        }
        return brand;
    }

    @Override
    public boolean existsBrandByName(String name) {
        String hql = "SELECT COUNT(b) FROM Brand b WHERE UPPER(b.name) = :name";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("name", name.toUpperCase())
                .getSingleResult();

        boolean exists = count != null && count > 0;
        logger.info("Brand with name: {} exists: {}", name, exists);
        return exists;
    }

    @Override
    public boolean existsBrandByNameAndNotId(String name, Integer id) {
        logger.info("Checking if brand with name: {} exists excluding id: {}", name, id);
        String hql = "SELECT COUNT(b) FROM Brand b WHERE UPPER(b.name) = :name AND b.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("name", name.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();

        boolean exists = count != null && count > 0;
        logger.info("Brand with name: {} exists excluding id {}: {}", name, id, exists);
        return exists;
    }


    @Override
    public List<Brand> listBrandsPage(int page, int size, String sortField, String sortDir) {
        logger.info("Listing brands page={}, size={}, sortField={}, sortDir={} from the database.",
                page, size, sortField, sortDir);

        int offset = page * size;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Brand> cq = cb.createQuery(Brand.class);
        Root<Brand> root = cq.from(Brand.class);

        Path<?> sortPath;
        switch (sortField) {
            case "id" -> sortPath = root.get("id");
            case "name" -> sortPath = root.get("name");
            case "country" -> sortPath = root.get("country");
            default -> {
                logger.warn("Unknown sortField '{}', defaulting to 'name'.", sortField);
                sortPath = root.get("name");
            }
        }

        boolean descending = "desc".equalsIgnoreCase(sortDir);
        Order order = descending ? cb.desc(sortPath) : cb.asc(sortPath);

        cq.select(root).orderBy(order);

        return entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long countBrands() {
        String hql = "SELECT COUNT(b) FROM Brand b";
        Long total = entityManager.createQuery(hql, Long.class).getSingleResult();
        return (total != null) ? total : 0L;
    }
}
