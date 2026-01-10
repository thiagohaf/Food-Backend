package com.thiagoferreira.food_backend.infraestructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Food API")
                        .description("API RESTful for Food App - Sistema de gestão de restaurantes. " +
                                "Esta API possui duas versões de autenticação: " +
                                "V1 utiliza autenticação stateful baseada em HttpSession (endpoints /v1/** e /auth/**). " +
                                "V2 utiliza autenticação baseada em JWT tokens (endpoints /v2/** e /v2/auth/**). " +
                                "Para usar V1, faça login através do endpoint POST /auth/login. A sessão é mantida automaticamente através de cookies (JSESSIONID). " +
                                "Para usar V2, faça login através do endpoint POST /v2/auth/login e use o token JWT retornado no header Authorization como 'Bearer {token}'. " +
                                "Os endpoints POST /v1/users e POST /v2/users (cadastro de usuário) são públicos e não requerem autenticação.")
                        .version("v2.0")
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")
                        )
                        .contact(new Contact()
                                .name("Thiago Ferreira")
                                .email("rm369442@fiap.com.br")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtido através do endpoint POST /v2/auth/login")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project README")
                        .url("/README.md")
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan("com.thiagoferreira.food_backend.controllers")
                .build();
    }

}
