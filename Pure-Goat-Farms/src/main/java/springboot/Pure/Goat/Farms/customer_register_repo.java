package springboot.Pure.Goat.Farms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface customer_register_repo extends JpaRepository<customer_register_model, String> {
    
    // ✅ Method to find customer by mobile number
    @Query("SELECT c FROM customer_register_model c WHERE c.mobile = :mobile")
    customer_register_model findByMobile(@Param("mobile") String mobile);
}