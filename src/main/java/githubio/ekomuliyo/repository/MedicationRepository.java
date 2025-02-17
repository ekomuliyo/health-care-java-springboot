package githubio.ekomuliyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import githubio.ekomuliyo.entity.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}