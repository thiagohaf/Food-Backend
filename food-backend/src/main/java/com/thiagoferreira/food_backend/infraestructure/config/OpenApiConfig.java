package com.thiagoferreira.food_backend.infraestructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtido através do endpoint POST /v2/auth/login")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project README")
                        .url("/README.md")
                );
    }

    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/v1/**", "/auth/**")
                .addOpenApiCustomizer(v1ApiCustomizer())
                .build();
    }

    @Bean
    public GroupedOpenApi v2Api() {
        return GroupedOpenApi.builder()
                .group("v2")
                .pathsToMatch("/v2/**")
                .addOpenApiCustomizer(v2ApiCustomizer())
                .build();
    }

    private OpenApiCustomizer v1ApiCustomizer() {
        return openApi -> openApi.info(new Info()
                .title("API Core - V1 (Legado)")
                .description("""
                        # API Core - V1 (Legado)
                        
                        API RESTful para o sistema de gestão de restaurantes Food App - Versão 1.
                        
                        ---
                        
                        ## 🔐 Autenticação
                        
                        Esta versão utiliza autenticação **stateful** baseada em **HttpSession**.
                        
                        **Endpoints:** `/v1/**` e `/auth/**`
                        
                        **Como usar:**
                        1. Faça login através do endpoint `POST /auth/login`
                        2. A sessão é mantida automaticamente através de cookies (JSESSIONID)
                        3. Não é necessário enviar tokens em requisições subsequentes
                        
                        ---
                        
                        ## ⚠️ Observações Importantes
                        
                        - **Endpoints públicos:** O endpoint `POST /v1/users` (cadastro de usuário) é público e **não requer autenticação**
                        - **Autenticação obrigatória:** Para acessar os demais endpoints protegidos, é necessário autenticar-se previamente
                        
                        ---
                        
                        ## 📝 Nota sobre Versão
                        
                        Esta é a versão legada da API. Recomendamos a migração para a **API Core - V2** que utiliza autenticação JWT e oferece melhor escalabilidade.
                        """)
                .version("v1.0")
                .license(new License()
                        .name("MIT")
                        .url("https://opensource.org/licenses/MIT")
                )
                .contact(new Contact()
                        .name("Thiago Ferreira")
                        .email("rm369442@fiap.com.br")
                )
        );
    }

    private OpenApiCustomizer v2ApiCustomizer() {
        return openApi -> openApi.info(new Info()
                .title("API Core - V2")
                .description("""
                        # API Core - V2
                        
                        API RESTful para o sistema de gestão de restaurantes Food App - Versão 2.
                        
                        ---
                        
                        ## 🔑 Autenticação
                        
                        Esta versão utiliza autenticação **stateless** baseada em **JWT** (JSON Web Token).
                        
                        **Endpoints:** `/v2/**`
                        
                        **Como usar:**
                        1. Faça login através do endpoint `POST /v2/auth/login`
                        2. Copie o token JWT retornado na resposta
                        3. Inclua o token no header `Authorization` de todas as requisições:
                           ```
                           Authorization: Bearer {seu_token_aqui}
                           ```
                        
                        ---
                        
                        ## ⚠️ Observações Importantes
                        
                        - **Endpoints públicos:** O endpoint `POST /v2/users` (cadastro de usuário) é público e **não requer autenticação**
                        - **Autenticação obrigatória:** Para acessar os demais endpoints protegidos, é necessário autenticar-se previamente
                        - **Token JWT:** Lembre-se de incluir o token Bearer no header `Authorization` em todas as requisições autenticadas
                        
                        ---
                        
                        ## 🚀 Primeiros Passos
                        
                        1. Cadastre um novo usuário através de `POST /v2/users` (público)
                        2. Autentique-se via `POST /v2/auth/login` com suas credenciais
                        3. Utilize o token JWT retornado para acessar os endpoints protegidos
                        
                        ---
                        
                        ## ✨ Vantagens da V2
                        
                        - Autenticação stateless (sem necessidade de sessão no servidor)
                        - Melhor escalabilidade e performance
                        - Tokens podem ser facilmente revogados
                        - Suporte a múltiplos dispositivos simultâneos
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
        );
    }

}
