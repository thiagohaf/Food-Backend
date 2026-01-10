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
                        .description("""
                                # Food API
                                
                                API RESTful para o sistema de gestão de restaurantes Food App.
                                
                                ---
                                
                                ## 📋 Versões Disponíveis
                                
                                Esta API oferece duas versões distintas, cada uma com seu próprio método de autenticação:
                                
                                ### 🔐 Versão 1 (V1)
                                
                                **Endpoints:** `/v1/**` e `/auth/**`
                                
                                **Autenticação:** Stateful baseada em HttpSession
                                
                                **Como usar:**
                                1. Faça login através do endpoint `POST /auth/login`
                                2. A sessão é mantida automaticamente através de cookies (JSESSIONID)
                                3. Não é necessário enviar tokens em requisições subsequentes
                                
                                ---
                                
                                ### 🔑 Versão 2 (V2) - Recomendada
                                
                                **Endpoints:** `/v2/**` e `/v2/auth/**`
                                
                                **Autenticação:** Stateless baseada em JWT (JSON Web Token)
                                
                                **Como usar:**
                                1. Faça login através do endpoint `POST /v2/auth/login`
                                2. Copie o token JWT retornado na resposta
                                3. Inclua o token no header `Authorization` de todas as requisições:
                                   ```
                                   Authorization: Bearer {seu_token_aqui}
                                   ```
                                
                                ---
                                
                                ## ⚠️ Observações Importantes
                                
                                - **Endpoints públicos:** Os endpoints `POST /v1/users` e `POST /v2/users` (cadastro de usuário) são públicos e **não requerem autenticação**
                                - **Autenticação obrigatória:** Para acessar os demais endpoints protegidos, é necessário autenticar-se previamente
                                - **Versão 2 (JWT):** Lembre-se de incluir o token Bearer no header `Authorization` em todas as requisições autenticadas
                                
                                ---
                                
                                ## 🚀 Primeiros Passos
                                
                                1. Cadastre um novo usuário através de `POST /v2/users` (público)
                                2. Autentique-se via `POST /v2/auth/login` com suas credenciais
                                3. Utilize o token JWT retornado para acessar os endpoints protegidos
                                
                                """)
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
