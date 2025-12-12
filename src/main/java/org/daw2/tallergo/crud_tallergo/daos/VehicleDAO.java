package org.daw2.tallergo.crud_tallergo.daos;

import org.daw2.tallergo.crud_tallergo.entities.Vehicle;
import java.util.List;

public interface VehicleDAO {
    List<Vehicle> listAllVehicles();
    List<Vehicle> listVehiclesPage(int page, int size, String sortField, String sortDir);
    long countVehicles();
    void insertVehicle(Vehicle vehicle);
    void updateVehicle(Vehicle vehicle);
    void deleteVehicle(Long id);
    Vehicle getVehicleById(Long id);

    boolean existsVehicleByMatricula(String matricula);
    boolean existsVehicleByMatriculaAndNotId(String matricula, Long id);
}
