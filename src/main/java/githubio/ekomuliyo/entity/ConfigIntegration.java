package githubio.ekomuliyo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "config_integration")
public class ConfigIntegration {

    @Id
    private Long id;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String orgId;
}