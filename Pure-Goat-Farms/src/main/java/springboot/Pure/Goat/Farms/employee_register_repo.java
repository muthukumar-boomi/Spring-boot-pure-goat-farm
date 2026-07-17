package springboot.Pure.Goat.Farms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface employee_register_repo extends JpaRepository<employee_register_model, String> {

    // Insert using stored procedure (register)
    @Transactional
    @Modifying
    @Query(value = "EXEC sp_employee_register :email, :mobile, :pass, :role", nativeQuery = true)
    void registerEmployee(@Param("email") String email,
                          @Param("mobile") String mobile,
                          @Param("pass") String pass,
                          @Param("role") String role);

    // Get all employees
    @Query(value = "EXEC sp_employee_get_all", nativeQuery = true)
    List<employee_register_model> getAllEmployees();

    // Update employee
    @Transactional
    @Modifying
    @Query(value = "EXEC sp_employee_update :email, :mobile, :pass, :role", nativeQuery = true)
    void updateEmployee(@Param("email") String email,
                        @Param("mobile") String mobile,
                        @Param("pass") String pass,
                        @Param("role") String role);

    // Delete employee
    @Transactional
    @Modifying
    @Query(value = "EXEC sp_employee_delete :email", nativeQuery = true)
    void deleteEmployee(@Param("email") String email);

    // Login: returns the employee record matching email and role (password check done in Java)
    @Query(value = "EXEC sp_employee_login :email, :role", nativeQuery = true)
    employee_register_model login(@Param("email") String email, @Param("role") String role);
}