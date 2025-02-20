package githubio.ekomuliyo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import githubio.ekomuliyo.dto.TokenResponse;
import githubio.ekomuliyo.dto.ClientCredentials;


@FeignClient(name = "authClient", url = "https://api-satusehat-stg.dto.kemkes.go.id/oauth2/v1")
public interface AuthClient {

    @PostMapping(value = "/accesstoken", consumes = "application/x-www-form-urlencoded")
    TokenResponse getAccessToken(
        @RequestHeader("Content-Type") String contentType,
        @RequestParam("grant_type") String grantType,
        @RequestBody ClientCredentials credentials
    );
}

