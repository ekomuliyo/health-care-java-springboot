package githubio.ekomuliyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import githubio.ekomuliyo.entity.Medication;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    
    boolean existsByName(String name);
    
    boolean existsBySku(String sku);
    
    Optional<Medication> findByName(String name);
    
    Optional<Medication> findBySku(String sku);
    
    List<Medication> findByManufacturer(String manufacturer);
    
    @Query("SELECT m FROM Medication m WHERE m.price <= :maxPrice")
    List<Medication> findByPriceLessThanEqual(@Param("maxPrice") Double maxPrice);
    
    @Query("SELECT m FROM Medication m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Medication> searchByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    @Query("SELECT DISTINCT m.manufacturer FROM Medication m")
    List<String> findAllManufacturers();
}