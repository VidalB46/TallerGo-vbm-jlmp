package org.daw2.tallergo.crud_tallergo.daos;

import org.daw2.tallergo.crud_tallergo.entities.Brand;
import java.util.List;

public interface BrandDAO {

    List<Brand> listAllBrands();
    List<Brand> listBrandsPage(int page, int size, String sortField, String sortDir);
    long countBrands();
    void insertBrand(Brand brand);
    void updateBrand(Brand brand);
    void deleteBrand(Integer id);
    Brand getBrandById(Integer id);

    boolean existsBrandByName(String name);
    boolean existsBrandByNameAndNotId(String name, Integer id);
}