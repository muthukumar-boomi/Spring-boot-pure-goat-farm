package springboot.Pure.Goat.Farms;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

public interface payment_repo extends JpaRepository<payment_model, String> {
    
    // Option 1: Direct INSERT query (if you don't want to use stored procedure)
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO payment (name, mobile, email, address, purpose, gender, qty, ctype, otype, pmethod, odate, otime, ddate, feedback, total) " +
                   "VALUES (:name, :mobile, :email, :address, :purpose, :gender, :qty, :ctype, :otype, :pmethod, :odate, :otime, :ddate, :feedback, :total)", 
           nativeQuery = true)
    void insert(
        @Param("name") String name,
        @Param("mobile") String mobile,
        @Param("email") String email,
        @Param("address") String address,
        @Param("purpose") String purpose,
        @Param("gender") String gender,
        @Param("qty") String qty,
        @Param("ctype") String ctype,
        @Param("otype") String otype,
        @Param("pmethod") String pmethod,
        @Param("odate") String odate,
        @Param("otime") String otime,
        @Param("ddate") String ddate,
        @Param("feedback") String feedback,
        @Param("total") String total);
    
    // Option 2: Using stored procedure
    @Transactional
    @Modifying
    @Query(value = "EXEC sp_payment_insert :name, :mobile, :email, :address, :purpose, :gender, :qty, :ctype, :otype, :pmethod, :odate, :otime, :ddate, :feedback, :total", 
           nativeQuery = true)
    void insertUsingSP(
        @Param("name") String name,
        @Param("mobile") String mobile,
        @Param("email") String email,
        @Param("address") String address,
        @Param("purpose") String purpose,
        @Param("gender") String gender,
        @Param("qty") String qty,
        @Param("ctype") String ctype,
        @Param("otype") String otype,
        @Param("pmethod") String pmethod,
        @Param("odate") String odate,
        @Param("otime") String otime,
        @Param("ddate") String ddate,
        @Param("feedback") String feedback,
        @Param("total") String total);
    
    // Custom queries for searching
    @Query(value = "SELECT * FROM payment WHERE name LIKE %:searchTerm% OR mobile LIKE %:searchTerm% OR email LIKE %:searchTerm%", 
           nativeQuery = true)
    List<payment_model> searchByNameOrMobileOrEmail(@Param("searchTerm") String searchTerm);
    
    @Query(value = "SELECT * FROM payment WHERE odate BETWEEN :startDate AND :endDate", 
           nativeQuery = true)
    List<payment_model> findByDateRange(@Param("startDate") String startDate, 
                                        @Param("endDate") String endDate);
    
    @Query(value = "SELECT * FROM payment WHERE purpose = :purpose", 
           nativeQuery = true)
    List<payment_model> findByPurpose(@Param("purpose") String purpose);
    
    // Statistics queries
    @Query(value = "SELECT SUM(CAST(total AS DECIMAL(10,2))) FROM payment", 
           nativeQuery = true)
    Double getTotalRevenue();
    
    @Query(value = "SELECT COUNT(*) FROM payment", 
           nativeQuery = true)
    Long getTotalPayments();
    
    @Query(value = "SELECT purpose, COUNT(*) as count FROM payment GROUP BY purpose", 
           nativeQuery = true)
    List<Object[]> getPaymentsByPurpose();
    
    // Update query
    @Transactional
    @Modifying
    @Query(value = "UPDATE payment SET mobile = :mobile, email = :email, address = :address, " +
                   "purpose = :purpose, gender = :gender, qty = :qty, ctype = :ctype, " +
                   "otype = :otype, pmethod = :pmethod, odate = :odate, otime = :otime, " +
                   "ddate = :ddate, feedback = :feedback, total = :total WHERE name = :name", 
           nativeQuery = true)
    void updatePayment(
        @Param("name") String name,
        @Param("mobile") String mobile,
        @Param("email") String email,
        @Param("address") String address,
        @Param("purpose") String purpose,
        @Param("gender") String gender,
        @Param("qty") String qty,
        @Param("ctype") String ctype,
        @Param("otype") String otype,
        @Param("pmethod") String pmethod,
        @Param("odate") String odate,
        @Param("otime") String otime,
        @Param("ddate") String ddate,
        @Param("feedback") String feedback,
        @Param("total") String total);
    
    // Delete query
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM payment WHERE name = :name", 
           nativeQuery = true)
    void deleteByName(@Param("name") String name);
    
    // Check if exists by mobile
    @Query(value = "SELECT COUNT(*) FROM payment WHERE mobile = :mobile", 
           nativeQuery = true)
    int countByMobile(@Param("mobile") String mobile);
    
    // Get recent payments
    @Query(value = "SELECT TOP 10 * FROM payment ORDER BY odate DESC, otime DESC", 
           nativeQuery = true)
    List<payment_model> findRecentPayments();
}