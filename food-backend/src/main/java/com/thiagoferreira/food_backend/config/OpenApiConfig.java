package com.thiagoferreira.food_backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Food API")
                        .description("API RESTful for Food.")
                        .version("v1.0")
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")
                        )
                        .contact(new Contact()
                                .name("Thiago Ferreira")
                                .email("rm369442@fiap.com.br")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project README")
                        .url("/README.md")
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .packagesToScan("com.thiagoferreira.food_backend.controllers")
                .build();
    }

}
