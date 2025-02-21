package githubio.ekomuliyo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl("/");
        devServer.setDescription("Server URL");

        Contact contact = new Contact();
        contact.setEmail("ekomuliyo@gmail.com");
        contact.setName("Eko Muliyo");
        contact.setUrl("https://github.com/ekomuliyo");

        License mitLicense = new License()
            .name("MIT License")
            .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("Healthcare Management API")
            .version("1.0")
            .contact(contact)
            .description("This API exposes endpoints for managing healthcare medications.")
            .license(mitLicense);

        Components components = new Components()
            .addRequestBodies("MedicationRequest", new RequestBody().content(
                new Content().addMediaType("multipart/form-data", 
                    new MediaType()
                        .schema(new Schema<>()
                            .type("object")
                            .addProperty("image", new Schema<>()
                                .type("string")
                                .format("binary"))
                            .addProperty("medication", new Schema<>()
                                .$ref("#/components/schemas/MedicationDto"))))));

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer))
            .components(components);
    }
}