package org.daw2.tallergo.crud_tallergo.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.daw2.tallergo.crud_tallergo.entities.Brand;
import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementación de {@link VehicleDAO}.
 * Gestiona las operaciones CRUD sobre la tabla `vehicles`,
 * incluyendo el mapeo del objeto anidado {@code Brand}.
 */
@Repository
@Transactional
public class VehicleDAOImpl implements VehicleDAO {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lista todos los vehículos (con su marca asociada mediante FETCH).
     */
    @Override
    public List<Vehicle> listAllVehicles() {
        logger.info("Listing all vehicles with their brands from the database.");
        // Importante: JOIN FETCH para traer los datos de la marca en la misma consulta
        String hql = "SELECT v FROM Vehicle v JOIN FETCH v.brand";
        List<Vehicle> vehicles = entityManager.createQuery(hql, Vehicle.class).getResultList();
        logger.info("Retrieved {} vehicles from the database.", vehicles.size());
        return vehicles;
    }

    /**
     * Verifica existencia por matrícula.
     */
    @Override
    public boolean existsVehicleByMatricula(String matricula) {
        logger.info("Checking if vehicle with matricula: {} exists", matricula);
        String hql = "SELECT COUNT(v) FROM Vehicle v WHERE UPPER(v.matricula) = :matricula";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("matricula", matricula.toUpperCase())
                .getSingleResult();

        boolean exists = count != null && count > 0;
        logger.info("Vehicle with matricula: {} exists: {}", matricula, exists);
        return exists;
    }

    @Override
    public void insertVehicle(Vehicle vehicle) {
        logger.info("Inserting vehicle with matricula: {}, model: {}, brandId: {}",
                vehicle.getMatricula(),
                vehicle.getModel(),
                vehicle.getBrand() != null ? vehicle.getBrand().getId() : null
        );
        entityManager.persist(vehicle);
        logger.info("Inserted vehicle with ID: {}", vehicle.getId());
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        logger.info("Retrieving vehicle by id: {}", id);
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        if (vehicle != null) {
            logger.info("Vehicle retrieved: {} - {}", vehicle.getMatricula(), vehicle.getModel());
        } else {
            logger.warn("No vehicle found with id: {}", id);
        }
        return vehicle;
    }

    @Override
    public boolean existsVehicleByMatriculaAndNotId(String matricula, Long id) {
        logger.info("Checking if vehicle with matricula: {} exists excluding id: {}", matricula, id);
        String hql = "SELECT COUNT(v) FROM Vehicle v WHERE UPPER(v.matricula) = :matricula AND v.id != :id";
        Long count = entityManager.createQuery(hql, Long.class)
                .setParameter("matricula", matricula.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();

        boolean exists = count != null && count > 0;
        logger.info("Vehicle with matricula: {} exists excluding id {}: {}", matricula, id, exists);
        return exists;
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {
        logger.info("Updating vehicle with id: {}", vehicle.getId());
        entityManager.merge(vehicle);
        logger.info("Updated vehicle with id: {}", vehicle.getId());
    }

    @Override
    public void deleteVehicle(Long id) {
        logger.info("Deleting vehicle with id: {}", id);
        Vehicle vehicle = entityManager.find(Vehicle.class, id);
        if (vehicle != null) {
            entityManager.remove(vehicle);
            logger.info("Deleted vehicle with id: {}", id);
        } else {
            logger.warn("Vehicle with id: {} not found.", id);
        }
    }

    /**
     * Paginación compleja con JOIN para ordenar por nombre de Marca.
     */
    @Override
    public List<Vehicle> listVehiclesPage(int page, int size, String sortField, String sortDir) {
        logger.info("Listing vehicles page={}, size={}, sortField={}, sortDir={} from the database.",
                page, size, sortField, sortDir);

        int offset = page * size;

        // 1. Construcción de Criteria
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vehicle> cq = cb.createQuery(Vehicle.class);
        Root<Vehicle> root = cq.from(Vehicle.class);

        // Hacemos fetch de brand para que venga cargada (equivalente al JOIN FETCH)
        root.fetch("brand", JoinType.INNER);

        // Join separado para poder usar brand.name en ORDER BY
        Join<Vehicle, Brand> brandJoin = root.join("brand", JoinType.INNER);

        // 2. Determinar el campo de ordenación
        Path<?> sortPath;
        switch (sortField) {
            case "id" -> sortPath = root.get("id");
            case "model" -> sortPath = root.get("model");
            case "matricula" -> sortPath = root.get("matricula");
            case "year" -> sortPath = root.get("year");
            // Aquí enlazamos con el nombre de la marca
            case "brand.name" -> sortPath = brandJoin.get("name");
            default -> {
                logger.warn("Unknown sortField '{}' for Vehicle, defaulting to 'model'.", sortField);
                sortPath = root.get("model");
            }
        }

        // 3. Dirección de ordenación
        boolean descending = "desc".equalsIgnoreCase(sortDir);
        Order order = descending ? cb.desc(sortPath) : cb.asc(sortPath);

        // 4. Aplicar ordenación
        cq.select(root).orderBy(order);

        // 5. Ejecutar
        return entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long countVehicles() {
        String hql = "SELECT COUNT(v) FROM Vehicle v";
        Long total = entityManager.createQuery(hql, Long.class).getSingleResult();
        return (total != null) ? total : 0L;
    }
}
