package githubio.ekomuliyo.service;

import githubio.ekomuliyo.client.AuthClient;
import githubio.ekomuliyo.dto.ClientCredentials;
import githubio.ekomuliyo.dto.TokenResponse;
import githubio.ekomuliyo.repository.ConfigIntegrationRepository;
import githubio.ekomuliyo.entity.ConfigIntegration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private ConfigIntegrationRepository configIntegrationRepository;

    @Transactional
    public TokenResponse getCredentials() {
        ConfigIntegration config = configIntegrationRepository.findFirstByOrderByIdAsc();
        if (config == null) {
            throw new RuntimeException("ConfigIntegration not found");
        }

        ClientCredentials credentials = new ClientCredentials();
        credentials.setClientId(config.getClientId());
        credentials.setClientSecret(config.getClientSecret());

        TokenResponse response = authClient.getAccessToken(
            "application/x-www-form-urlencoded",
            "client_credentials",
            credentials
        );
        response.setOrgId(config.getOrgId());

        config.setAccessToken(response.getAccessToken());
        configIntegrationRepository.saveAndFlush(config);

        return response;
    }
}