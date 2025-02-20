package githubio.ekomuliyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import githubio.ekomuliyo.entity.ConfigIntegration;

public interface ConfigIntegrationRepository extends JpaRepository<ConfigIntegration, Long> {
    ConfigIntegration findFirstByOrderByIdAsc();
}