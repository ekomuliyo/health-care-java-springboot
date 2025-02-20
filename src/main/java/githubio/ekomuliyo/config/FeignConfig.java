package githubio.ekomuliyo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Configuration
@EnableFeignClients(basePackages = {
    "githubio.ekomuliyo.client"
})
public class FeignConfig {
}