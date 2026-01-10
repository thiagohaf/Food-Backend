
# Food App Backend

Sistema de gestão de restaurantes - Tech Challenge Fase 1

## 📋 Sobre o Projeto

API RESTful desenvolvida em Spring Boot para gerenciamento de usuários do sistema Food App. O projeto implementa operações CRUD completas para usuários, com validações, tratamento de exceções padronizado usando RFC 7807 (Problem Details), e documentação automática via OpenAPI/Swagger.

## 🛠️ Tecnologias Utilizadas

### Framework e Core
- **Spring Boot 4.0.1** - Framework principal
- **Java 21** - Linguagem de programação
- **Maven** - Gerenciador de dependências

### Persistência de Dados
- **Spring Data JPA** - Camada de persistência
- **PostgreSQL** - Banco de dados principal
- **Hibernate** - ORM

### Validação e Documentação
- **Bean Validation** - Validação de entradas
- **SpringDoc OpenAPI 3 (v2.7.0)** - Documentação da API (Swagger)
- **Swagger Annotations (v2.2.22)** - Anotações para documentação

### Segurança
- **jBCrypt (v0.4)** - Biblioteca para hashing de senhas (BCrypt)
- **HttpSession** - Autenticação stateful baseada em sessão (V1)
- **Spring Security** - Framework de segurança (V2)
- **JWT (jjwt 0.12.5)** - JSON Web Tokens para autenticação stateless (V2)

### Utilitários
- **Lombok** - Redução de boilerplate

### Testes e Qualidade
- **JUnit 5** - Framework de testes (via Spring Boot Starter Test)
- **JaCoCo (v0.8.11)** - Análise de cobertura de código (mínimo 80%)
- **Maven Surefire Plugin** - Execução de testes

### Containerização
- **Docker** - Containerização da aplicação
- **Docker Compose** - Orquestração de containers

## 📁 Estrutura do Projeto

```
food-backend/
├── src/
│   ├── main/
│   │   ├── java/com/thiagoferreira/food_backend/
│   │   │   ├── Application.java                    # Classe principal
│   │   │   ├── controllers/                        # Controladores REST
│   │   │   │   ├── AuthController.java
│   │   │   │   └── UserController.java
│   │   │   ├── interceptors/                       # Interceptadores HTTP
│   │   │   │   └── AuthInterceptor.java
│   │   │   ├── domain/
│   │   │   │   ├── dto/                           # Data Transfer Objects
│   │   │   │   │   ├── AddressDTO.java
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── PasswordChangeRequest.java
│   │   │   │   │   ├── ProblemDetailDTO.java
│   │   │   │   │   ├── UserRequest.java
│   │   │   │   │   ├── UserResponse.java
│   │   │   │   │   └── UserUpdateRequest.java
│   │   │   │   ├── entities/                      # Entidades JPA
│   │   │   │   │   ├── Address.java
│   │   │   │   │   └── User.java
│   │   │   │   └── enums/                         # Enumeradores
│   │   │   │       ├── ErrorMessages.java
│   │   │   │       └── UserType.java
│   │   │   ├── exceptions/                        # Tratamento de exceções
│   │   │   │   ├── DomainValidationException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   ├── infraestructure/
│   │   │   │   ├── config/                        # Configurações
│   │   │   │   │   ├── OpenApiConfig.java
│   │   │   │   │   └── WebConfig.java
│   │   │   │   └── repositories/                  # Repositórios JPA
│   │   │   │       └── UserRepository.java
│   │   │   ├── mappers/                           # Mappers DTO/Entity
│   │   │   │   └── UserMapper.java
│   │   │   └── services/                          # Lógica de negócio
│   │   │       └── UserService.java
│   │   └── resources/
│   │       └── application.properties             # Configurações da aplicação
│   └── test/                                      # Testes
├── docker-compose.yml                             # Configuração Docker Compose
├── Dockerfile                                     # Imagem Docker
├── pom.xml                                        # Configuração Maven
└── README.md                                      # Este arquivo
```

## 🚀 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 21** ou superior
- **Maven 3.9** ou superior (opcional - o projeto inclui Maven Wrapper)
- **PostgreSQL 16** ou superior (ou Docker)
- **Docker** e **Docker Compose** (opcional, para execução via containers)

**Nota**: O projeto inclui Maven Wrapper (`mvnw` e `mvnw.cmd`), então você não precisa ter Maven instalado localmente se preferir usar o wrapper.

## ⚙️ Configuração

### Variáveis de Ambiente

O projeto utiliza variáveis de ambiente para configuração. Você pode configurá-las através de variáveis de ambiente ou editar o arquivo `application.properties`.

**Variáveis disponíveis:**

- `DB_HOST` - Host do PostgreSQL (padrão: `localhost`)
- `DB_PORT` - Porta do PostgreSQL (padrão: `5432`)
- `DB_NAME` - Nome do banco de dados (padrão: `food_db`)
- `DB_USER` - Usuário do banco de dados (padrão: `postgres`)
- `DB_PASSWORD` - Senha do banco de dados (padrão: `postgres`)
- `SERVER_PORT` - Porta da aplicação (padrão: `8080`)

## 🏃 Executando a Aplicação

### Opção 1: Execução Local com Maven

1. **Clone o repositório** (se ainda não fez):
```bash
git clone <url-do-repositorio>
cd food-backend
```

2. **Configure o banco de dados PostgreSQL**:
   - Crie um banco de dados chamado `food_db`
   - Ou ajuste as variáveis de ambiente conforme necessário

3. **Execute a aplicação**:
```bash
./mvnw spring-boot:run
```

Ou usando Maven instalado localmente:
```bash
mvn spring-boot:run
```

4. **Acesse a aplicação**:
   - API: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Docs: `http://localhost:8080/api-docs`

### Opção 2: Execução com Docker Compose

1. **Execute o Docker Compose**:
```bash
docker-compose up --build
```

2. **Acesse a aplicação**:
   - API: `http://localhost:8081`
   - Swagger UI: `http://localhost:8081/swagger-ui.html`
   - API Docs: `http://localhost:8081/api-docs`

### Opção 3: Build e Execução do JAR

1. **Construa o projeto**:
```bash
./mvnw clean package
```

2. **Execute o JAR gerado**:
```bash
java -jar target/food-backend-0.0.1-SNAPSHOT.jar
```

## 📚 Documentação da API

A documentação completa da API está disponível através do **Swagger UI** quando a aplicação estiver em execução:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

### Endpoints Principais

#### Autenticação V1 (`/auth`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/auth/login` | Autenticar usuário e criar sessão | Não requerida |
| POST | `/auth/logout` | Encerrar sessão do usuário | Requerida |

**Login Request:**
```json
{
  "login": "usuario123",
  "password": "senha123"
}
```

**Nota:** Após o login bem-sucedido, uma sessão HTTP é criada e o ID do usuário é armazenado na sessão. Esta sessão deve ser mantida pelo cliente (cookies) para acessar endpoints protegidos.

#### Usuários V1 (`/v1/users`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/v1/users` | Criar novo usuário | Não requerida (público) |
| GET | `/v1/users` | Listar todos os usuários | Requerida |
| GET | `/v1/users/{id}` | Buscar usuário por ID | Requerida |
| GET | `/v1/users/search/name?name={nome}` | Buscar usuários por nome | Requerida |
| GET | `/v1/users/search/login?login={login}` | Buscar usuário por login | Requerida |
| GET | `/v1/users/search/email?email={email}` | Buscar usuário por email | Requerida |
| PUT | `/v1/users/{id}` | Atualizar informações do usuário | Requerida |
| PATCH | `/v1/users/{id}/password` | Alterar senha do usuário | Requerida |
| DELETE | `/v1/users/{id}` | Deletar usuário | Requerida |

#### Autenticação V2 (`/v2/auth`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/v2/auth/login` | Autenticar usuário e obter JWT token | Não requerida |

**Login Request:**
```json
{
  "login": "usuario123",
  "password": "senha123"
}
```

**Login Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

**Nota:** Após o login bem-sucedido, use o token retornado no header `Authorization: Bearer {token}` para acessar endpoints protegidos.

#### Usuários V2 (`/v2/users`)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/v2/users` | Criar novo usuário | Não requerida (público) |
| GET | `/v2/users` | Listar todos os usuários | Requerida (JWT) |
| GET | `/v2/users/{id}` | Buscar usuário por ID | Requerida (JWT) |
| GET | `/v2/users/search/name?name={nome}` | Buscar usuários por nome | Requerida (JWT) |
| GET | `/v2/users/search/login?login={login}` | Buscar usuário por login | Requerida (JWT) |
| GET | `/v2/users/search/email?email={email}` | Buscar usuário por email | Requerida (JWT) |
| PUT | `/v2/users/{id}` | Atualizar informações do usuário | Requerida (JWT) |
| PATCH | `/v2/users/{id}/password` | Alterar senha do usuário | Requerida (JWT) |
| DELETE | `/v2/users/{id}` | Deletar usuário | Requerida (JWT) |

**Nota:** Todos os erros nos endpoints V2 retornam **ProblemDetail (RFC 7807)**, incluindo erros de autenticação do Spring Security.

### Modelo de Dados

#### User
- `id` (Long) - Identificador único
- `name` (String) - Nome do usuário
- `email` (String) - Email único do usuário
- `login` (String) - Login único do usuário
- `password` (String) - Senha do usuário
- `type` (UserType) - Tipo de usuário (OWNER ou CUSTOMER)
- `address` (Address) - Endereço do usuário
- `createdAt` (LocalDateTime) - Data de criação
- `lastUpdated` (LocalDateTime) - Data da última atualização

#### Address
- `street` (String) - Rua
- `number` (String) - Número
- `city` (String) - Cidade
- `zipCode` (String) - CEP

#### UserType (Enum)
- `OWNER` - Proprietário
- `CUSTOMER` - Cliente

## 🔐 Autenticação e Segurança

A aplicação possui **duas versões de autenticação**:

### Versão 1 (V1) - HttpSession

A aplicação implementa autenticação **stateful** baseada em **HttpSession**, sem utilizar Spring Security. A proteção dos endpoints é feita manualmente através de um `HandlerInterceptor`.

### Como Funciona

1. **Login**: O usuário faz uma requisição `POST /auth/login` com login e senha
2. **Validação**: O sistema busca o usuário pelo login e verifica a senha usando BCrypt
3. **Sessão**: Se válido, uma sessão HTTP é criada com o atributo `USER_ID`
4. **Acesso**: Endpoints protegidos verificam a existência da sessão válida
5. **Logout**: O usuário pode encerrar a sessão através de `POST /auth/logout`

### Endpoints Públicos

Os seguintes endpoints **não requerem** autenticação:
- `POST /auth/login` - Login de usuário
- `POST /v1/users` - Cadastro de novo usuário (público)
- `OPTIONS` - Requisições CORS preflight

### Endpoints Protegidos

Todos os demais endpoints requerem autenticação. Se uma requisição for feita sem sessão válida, será retornado **401 Unauthorized** com um objeto ProblemDetail no formato RFC 7807:

```json
{
  "type": "https://api.food-backend.com/problems/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Authentication required. Please log in to access this resource."
}
```

### Criptografia de Senhas

As senhas são criptografadas usando **BCrypt** antes de serem armazenadas no banco de dados:
- Hash gerado automaticamente no cadastro (`createUser`)
- Verificação de senha no login usando `BCrypt.checkpw()`
- Salt automático gerado para cada senha

### Exemplo de Fluxo

```bash
# 1. Criar usuário (público)
POST /v1/users
{
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "password": "senha123",
  "type": "CUSTOMER"
}

# 2. Fazer login (cria sessão)
POST /auth/login
{
  "login": "joaosilva",
  "password": "senha123"
}
# Resposta: 200 OK (sessão criada automaticamente)

# 3. Acessar endpoints protegidos (sessão é mantida automaticamente)
GET /v1/users
# Resposta: 200 OK com lista de usuários

# 4. Logout
POST /auth/logout
# Resposta: 200 OK (sessão invalidada)
```

### Versão 2 (V2) - JWT com Spring Security

A versão 2 dos endpoints implementa autenticação **stateless** baseada em **JWT (JSON Web Tokens)** usando Spring Security. Todos os erros retornam **ProblemDetail (RFC 7807)**.

#### Como Funciona

1. **Login**: O usuário faz uma requisição `POST /v2/auth/login` com login e senha
2. **Validação**: O sistema busca o usuário pelo login e verifica a senha usando BCrypt
3. **Token JWT**: Se válido, retorna um token JWT no formato `{"token": "...", "type": "Bearer"}`
4. **Acesso**: Endpoints protegidos requerem o header `Authorization: Bearer {token}`
5. **Validação**: O Spring Security valida o token JWT automaticamente

#### Endpoints Públicos V2

Os seguintes endpoints **não requerem** autenticação:
- `POST /v2/auth/login` - Login de usuário (retorna JWT token)
- `POST /v2/users` - Cadastro de novo usuário (público)

#### Endpoints Protegidos V2

Todos os demais endpoints `/v2/**` requerem autenticação via JWT. Se uma requisição for feita sem token válido, será retornado **401 Unauthorized** com um objeto ProblemDetail no formato RFC 7807:

```json
{
  "type": "https://api.food-backend.com/problems/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Authentication required. Please provide a valid JWT token in the Authorization header."
}
```

#### Endpoints V2

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| POST | `/v2/auth/login` | Login e obtenção de JWT token | Não requerida |
| POST | `/v2/users` | Criar novo usuário | Não requerida (público) |
| GET | `/v2/users` | Listar todos os usuários | Requerida (JWT) |
| GET | `/v2/users/{id}` | Buscar usuário por ID | Requerida (JWT) |
| GET | `/v2/users/search/name?name={nome}` | Buscar usuários por nome | Requerida (JWT) |
| GET | `/v2/users/search/login?login={login}` | Buscar usuário por login | Requerida (JWT) |
| GET | `/v2/users/search/email?email={email}` | Buscar usuário por email | Requerida (JWT) |
| PUT | `/v2/users/{id}` | Atualizar informações do usuário | Requerida (JWT) |
| PATCH | `/v2/users/{id}/password` | Alterar senha do usuário | Requerida (JWT) |
| DELETE | `/v2/users/{id}` | Deletar usuário | Requerida (JWT) |

#### Exemplo de Fluxo V2

```bash
# 1. Criar usuário (público)
POST /v2/users
{
  "name": "Maria Santos",
  "email": "maria@email.com",
  "login": "mariasantos",
  "password": "senha123",
  "type": "CUSTOMER"
}
# Resposta: 201 Created

# 2. Fazer login v2 (obter token JWT)
POST /v2/auth/login
{
  "login": "mariasantos",
  "password": "senha123"
}
# Resposta: 200 OK
# {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "type": "Bearer"
# }

# 3. Acessar endpoints protegidos (usar token no header)
GET /v2/users
Headers: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
# Resposta: 200 OK com lista de usuários
```

#### Configuração JWT

As propriedades JWT podem ser configuradas no `application.properties`:
- `jwt.secret`: Chave secreta para assinar tokens (padrão: valor padrão seguro)
- `jwt.expiration`: Tempo de expiração em milissegundos (padrão: 86400000 = 24 horas)

Ou via variáveis de ambiente:
- `JWT_SECRET`: Chave secreta para assinar tokens
- `JWT_EXPIRATION`: Tempo de expiração em milissegundos

## 🔒 Tratamento de Erros

A aplicação utiliza **RFC 7807 (Problem Details)** para padronização de respostas de erro. Todas as exceções são tratadas pelo `GlobalExceptionHandler` e retornam objetos `ProblemDetail` estruturados.

### Tipos de Erros Tratados

- **400 Bad Request**: Validações, violações de domínio, parâmetros inválidos
- **401 Unauthorized**: Acesso não autorizado (sessão inválida ou ausente)
- **404 Not Found**: Recurso não encontrado
- **405 Method Not Allowed**: Método HTTP não suportado
- **415 Unsupported Media Type**: Tipo de mídia não suportado
- **500 Internal Server Error**: Erros internos do servidor

### Exemplos de Respostas de Erro

**Exemplo 1: Erro de Validação (400)**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "email": "Invalid email format",
    "password": "Password must be at least 6 characters"
  }
}
```

**Exemplo 2: Acesso Não Autorizado (401)**
```json
{
  "type": "https://api.food-backend.com/problems/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Authentication required. Please log in to access this resource."
}
```

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

1. **Controller Layer** - Recebe requisições HTTP e delega para serviços
2. **Service Layer** - Contém a lógica de negócio
3. **Repository Layer** - Acesso aos dados (Spring Data JPA)
4. **Entity Layer** - Modelos de domínio
5. **DTO Layer** - Objetos de transferência de dados
6. **Exception Handler** - Tratamento centralizado de exceções

### Padrões Utilizados

- **DTO Pattern** - Separação entre entidades de domínio e objetos de transferência
- **Mapper Pattern** - Conversão entre DTOs e entidades
- **Repository Pattern** - Abstração de acesso a dados
- **Service Layer Pattern** - Isolamento da lógica de negócio
- **Exception Handler Pattern** - Tratamento centralizado de exceções

## 🧪 Testes

### Executando Testes

Para executar os testes:

```bash
./mvnw test
```

Ou usando Maven instalado localmente:
```bash
mvn test
```

### Cobertura de Código

O projeto utiliza **JaCoCo** para análise de cobertura de código:

- **Cobertura mínima exigida**: 80% de linhas
- **Relatório gerado**: `target/site/jacoco/index.html`

Para visualizar o relatório de cobertura após executar os testes:

```bash
# Os relatórios são gerados automaticamente após mvn test
# Acesse: target/site/jacoco/index.html
```

Para executar os testes e verificar a cobertura:

```bash
./mvnw clean test
```

### Tipos de Testes

O projeto inclui:
- **Testes unitários** - Testes isolados de componentes individuais
- **Testes de integração** - Testes via Spring Boot Test com contexto completo
- **Testes de controladores** - Testes de endpoints REST
- **Testes de serviços** - Testes de lógica de negócio
- **Testes de exceções** - Testes de tratamento de erros

### Testes com Postman

Uma coleção do Postman está disponível no arquivo:
- `Food_Backend_ProblemDetail_Tests.postman_collection.json`

Consulte o arquivo `POSTMAN_TEST_GUIDE.md` para mais detalhes sobre como usar a coleção de testes.

## 🐳 Docker

### Dockerfile

A aplicação possui um `Dockerfile` multi-stage que:
1. **Stage 1 (Build)**: Usa Maven 3.9 com Eclipse Temurin 21 para compilar a aplicação
2. **Stage 2 (Runtime)**: Cria uma imagem final com Eclipse Temurin 21 JRE apenas
3. Expõe a porta 8080 internamente (mapeada para 8081 no host via Docker Compose)
4. Executa o JAR gerado automaticamente

### Docker Compose

O `docker-compose.yml` inclui dois serviços:

#### Serviço PostgreSQL
- **Imagem**: `postgres:16-alpine`
- **Container**: `food-postgres`
- **Porta**: `5432:5432`
- **Banco de dados**: `food_db`
- **Usuário**: `postgres`
- **Senha**: `postgres`
- **Volume persistente**: `postgres_data`
- **Health check**: Verifica se o PostgreSQL está pronto

#### Serviço App
- **Build**: Usa o Dockerfile local
- **Container**: `food-app`
- **Porta**: `8081:8080` (host:container)
- **Dependências**: Aguarda o PostgreSQL estar saudável
- **Variáveis de ambiente**: Configuradas automaticamente

O Docker Compose configura automaticamente:
- **Health checks** - Verifica saúde dos serviços
- **Volumes persistentes** - Dados do PostgreSQL são mantidos
- **Rede interna** - Comunicação entre containers
- **Dependências** - App aguarda PostgreSQL estar pronto

## 📦 Build

### Build Local

Para construir o projeto sem executar testes:

```bash
./mvnw clean package -DskipTests
```

Para construir o projeto com testes:

```bash
./mvnw clean package
```

### Build Docker

Para construir a imagem Docker:

```bash
cd food-backend
docker build -t food-backend:latest .
```

### Build Multi-Stage

O Dockerfile utiliza build multi-stage:
- **Build stage**: Compila o projeto usando Maven
- **Runtime stage**: Imagem final otimizada com apenas JRE

## 🔧 Configurações Adicionais

### JPA/Hibernate

- **DDL Auto**: `update` (atualiza schema automaticamente)
- **Show SQL**: `false` (pode ser habilitado para debug)
- **Format SQL**: `true` (SQL formatado quando exibido)
- **Open-in-View**: `false` (melhor prática para evitar problemas de performance)
- **Dialect**: PostgreSQL

### Swagger/OpenAPI

- **Versão**: SpringDoc OpenAPI 2.7.0
- **Path da documentação**: `/api-docs`
- **Path do Swagger UI**: `/swagger-ui.html`
- **Ordenação**: Por método HTTP
- **Tags**: Ordenadas alfabeticamente
- **Swagger Annotations**: v2.2.22

### Cobertura de Código (JaCoCo)

- **Plugin**: JaCoCo Maven Plugin v0.8.11
- **Cobertura mínima**: 80% de linhas
- **Relatórios**: Gerados em `target/site/jacoco/`
- **Verificação**: Executada automaticamente durante `mvn test`

## 📝 Validações Implementadas

### Validações de Entrada

- **Email**: Formato válido de email (Bean Validation)
- **Password**: Mínimo de 6 caracteres
- **Campos obrigatórios**: Name, Email, Login, Password, UserType
- **Unicidade**: Email e Login devem ser únicos

### Validações de Domínio

- Email não pode ser duplicado
- Login não pode ser duplicado
- Senha atual e nova senha não podem ser iguais
- Usuário deve existir para operações de atualização/exclusão
- Senha deve ser verificada corretamente no login (BCrypt)

## 🤝 Contribuindo

Este é um projeto acadêmico desenvolvido como parte do Tech Challenge Fase 1 da FIAP.

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo de licença para mais detalhes.

## 👤 Autor

**Thiago Ferreira**
- Email: rm369442@fiap.com.br

## 📞 Suporte

Para questões ou suporte, consulte:
- Documentação do Swagger UI: `http://localhost:8080/swagger-ui.html`
- Arquivo `POSTMAN_TEST_GUIDE.md` para guia de testes
- Arquivo `HELP.md` para referências técnicas

## 🔄 Versão

- **Versão atual**: 0.0.1-SNAPSHOT
- **Spring Boot**: 4.0.1
- **Java**: 21
- **Maven**: 3.9+
- **PostgreSQL**: 16
- **SpringDoc OpenAPI**: 2.7.0
- **Swagger Annotations**: 2.2.22
- **JaCoCo**: 0.8.11
- **jBCrypt**: 0.4
- **Spring Security**: (incluído no Spring Boot 4.0.1)
- **JWT (jjwt)**: 0.12.5

---

**Nota**: Este projeto faz parte do Tech Challenge Fase 1 - Sistema de gestão de restaurantes.

