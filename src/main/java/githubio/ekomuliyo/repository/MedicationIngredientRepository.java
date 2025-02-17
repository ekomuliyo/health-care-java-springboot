package githubio.ekomuliyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import githubio.ekomuliyo.entity.MedicationIngredient;

public interface MedicationIngredientRepository extends JpaRepository<MedicationIngredient, Long> {
}