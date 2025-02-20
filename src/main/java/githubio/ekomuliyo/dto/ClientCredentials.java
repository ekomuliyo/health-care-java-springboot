package githubio.ekomuliyo.dto;

import feign.form.FormProperty;
import lombok.Data;

@Data
public class ClientCredentials {
    @FormProperty("client_id")
    private String clientId;
    
    @FormProperty("client_secret")
    private String clientSecret;
}