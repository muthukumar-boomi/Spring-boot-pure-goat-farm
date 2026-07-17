package springboot.Pure.Goat.Farms;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

public interface product_catalog_repo extends JpaRepository<product_catalog_model, String> {
    
    // Insert using stored procedure (if exists)
    @Transactional
    @Modifying
    @Query(value = "EXEC sp_product_catalog_insert :name, :code, :purpose, :gender, :weight, :height, :storage, :price, :total, :grade, :stack", 
           nativeQuery = true)
    void insert(
        @Param("name") String name,
        @Param("code") String code,
        @Param("purpose") String purpose,
        @Param("gender") String gender,
        @Param("weight") String weight,
        @Param("height") String height,
        @Param("storage") String storage,
        @Param("price") String price,
        @Param("total") String total,
        @Param("grade") String grade,
        @Param("stack") String stack);

    // Get all (stored procedure)
    @Query(value = "EXEC sp_product_catalog_get_all", nativeQuery = true)
    List<product_catalog_model> get_all();
    
    // Get by name (stored procedure)
    @Query(value = "EXEC sp_product_catalog_get_by_name :name", nativeQuery = true)
    product_catalog_model get_by_name(@Param("name") String name);
    
    // Get by code – STORED PROCEDURE (may be missing)
    @Query(value = "EXEC sp_product_catalog_get_by_code :code", nativeQuery = true)
    product_catalog_model get_by_code(@Param("code") String code);
    
    // ✅ JPQL FALLBACK – works without stored procedure
    @Query("SELECT p FROM product_catalog_model p WHERE p.code = :code")
    product_catalog_model findByCode(@Param("code") String code);
    
    // Update (stored procedure)
    @Transactional
    @Modifying
    @Query(value = "EXEC sp_product_catalog_update :name, :code, :purpose, :gender, :weight, :height, :storage, :price, :total, :grade, :stack", 
           nativeQuery = true)
    void update(
        @Param("name") String name,
        @Param("code") String code,
        @Param("purpose") String purpose,
        @Param("gender") String gender,
        @Param("weight") String weight,
        @Param("height") String height,
        @Param("storage") String storage,
        @Param("price") String price,
        @Param("total") String total,
        @Param("grade") String grade,
        @Param("stack") String stack);
    
    // Delete (stored procedure)
    @Transactional
    @Modifying
    @Query(value = "EXEC sp_product_catalog_delete :name", nativeQuery = true)
    void delete(@Param("name") String name);
    
    // Search (stored procedure)
    @Query(value = "EXEC sp_product_catalog_search :name, :code, :purpose, :gender, :grade", 
           nativeQuery = true)
    List<product_catalog_model> search(
        @Param("name") String name,
        @Param("code") String code,
        @Param("purpose") String purpose,
        @Param("gender") String gender,
        @Param("grade") String grade);
    
    // Get by purpose (stored procedure)
    @Query(value = "EXEC sp_product_catalog_get_by_purpose :purpose", 
           nativeQuery = true)
    List<product_catalog_model> get_by_purpose(@Param("purpose") String purpose);
}