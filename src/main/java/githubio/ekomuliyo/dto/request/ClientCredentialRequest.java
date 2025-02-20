package githubio.ekomuliyo.dto.request;

import feign.form.FormProperty;
import lombok.Data;

@Data
public class ClientCredentialRequest {
    @FormProperty("client_id")
    private String clientId;
    
    @FormProperty("client_secret")
    private String clientSecret;
}