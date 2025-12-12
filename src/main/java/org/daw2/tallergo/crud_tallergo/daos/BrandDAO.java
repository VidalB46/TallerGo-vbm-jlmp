package org.daw2.tallergo.crud_tallergo.daos;

import org.daw2.tallergo.crud_tallergo.entities.Brand;
import java.util.List;

public interface BrandDAO {

    List<Brand> listAllBrands();
    List<Brand> listBrandsPage(int page, int size, String sortField, String sortDir);
    long countBrands();
    void insertBrand(Brand brand);
    void updateBrand(Brand brand);
    void deleteBrand(Integer id); // Ojo: Brand ID es Integer según tu schema
    Brand getBrandById(Integer id);

    // En Brand el campo único es 'name' (en Region era 'code')
    boolean existsBrandByName(String name);
    boolean existsBrandByNameAndNotId(String name, Integer id);
}