package springboot.Pure.Goat.Farms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface goat_breed_repo extends JpaRepository<goat_breed_model, String> {
    
    // Find by name (since name is the ID, this is same as findById)
    goat_breed_model findByName(String name);
    
    // Search by name, code, or purpose
    List<goat_breed_model> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCaseOrPurposeContainingIgnoreCase(
        String name, String code, String purpose);
    
    // Find by purpose
    List<goat_breed_model> findByPurpose(String purpose);
    
    // Find by origin
    List<goat_breed_model> findByOrgin(String orgin);
}