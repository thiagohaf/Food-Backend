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
- **SpringDoc OpenAPI 3** - Documentação da API (Swagger)
- **Swagger Annotations** - Anotações para documentação

### Utilitários
- **Lombok** - Redução de boilerplate
- **Spring Boot DevTools** - Ferramentas de desenvolvimento

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
│   │   │   │   └── UserController.java
│   │   │   ├── domain/
│   │   │   │   ├── dto/                           # Data Transfer Objects
│   │   │   │   │   ├── AddressDTO.java
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
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── infraestructure/
│   │   │   │   ├── config/                        # Configurações
│   │   │   │   │   └── OpenApiConfig.java
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
- **Maven 3.9** ou superior
- **PostgreSQL 16** ou superior (ou Docker)
- **Docker** e **Docker Compose** (opcional, para execução via containers)

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

#### Usuários (`/v1/users`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/v1/users` | Criar novo usuário |
| GET | `/v1/users` | Listar todos os usuários |
| GET | `/v1/users/{id}` | Buscar usuário por ID |
| GET | `/v1/users/search/name?name={nome}` | Buscar usuários por nome |
| GET | `/v1/users/search/login?login={login}` | Buscar usuário por login |
| GET | `/v1/users/search/email?email={email}` | Buscar usuário por email |
| PUT | `/v1/users/{id}` | Atualizar informações do usuário |
| PATCH | `/v1/users/{id}/password` | Alterar senha do usuário |
| DELETE | `/v1/users/{id}` | Deletar usuário |

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

## 🔒 Tratamento de Erros

A aplicação utiliza **RFC 7807 (Problem Details)** para padronização de respostas de erro. Todas as exceções são tratadas pelo `GlobalExceptionHandler` e retornam objetos `ProblemDetail` estruturados.

### Tipos de Erros Tratados

- **400 Bad Request**: Validações, violações de domínio, parâmetros inválidos
- **404 Not Found**: Recurso não encontrado
- **405 Method Not Allowed**: Método HTTP não suportado
- **415 Unsupported Media Type**: Tipo de mídia não suportado
- **500 Internal Server Error**: Erros internos do servidor

### Exemplo de Resposta de Erro

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

Para executar os testes:

```bash
./mvnw test
```

O projeto inclui:
- Testes unitários
- Testes de integração (via Spring Boot Test)

### Testes com Postman

Uma coleção do Postman está disponível no arquivo:
- `Food_Backend_ProblemDetail_Tests.postman_collection.json`

Consulte o arquivo `POSTMAN_TEST_GUIDE.md` para mais detalhes sobre como usar a coleção de testes.

## 🐳 Docker

### Dockerfile

A aplicação possui um `Dockerfile` multi-stage que:
1. Usa Maven para compilar a aplicação
2. Cria uma imagem final com JRE apenas
3. Expõe a porta 8081

### Docker Compose

O `docker-compose.yml` inclui:
- **PostgreSQL 16** - Banco de dados
- **App** - Aplicação Spring Boot

O Docker Compose configura automaticamente:
- Banco de dados PostgreSQL
- Health checks
- Volumes persistentes
- Rede entre containers

## 📦 Build

Para construir o projeto sem executar testes:

```bash
./mvnw clean package -DskipTests
```

Para construir a imagem Docker:

```bash
docker build -t food-backend:latest .
```

## 🔧 Configurações Adicionais

### JPA/Hibernate

- DDL Auto: `update` (atualiza schema automaticamente)
- Show SQL: `false` (pode ser habilitado para debug)
- Dialect: PostgreSQL

### Swagger/OpenAPI

- Path da documentação: `/api-docs`
- Path do Swagger UI: `/swagger-ui.html`
- Ordenação: Por método HTTP
- Tags ordenadas alfabeticamente

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

---

**Nota**: Este projeto faz parte do Tech Challenge Fase 1 - Sistema de gestão de restaurantes.

