# Guia de Testes - ProblemDetail (RFC 7807)

Este documento contém todos os cenários de teste para validar a implementação do tratamento de erros com ProblemDetail (RFC 7807) via Postman.

**Base URL:** `http://localhost:8080`

## 📑 Índice

0. [Autenticação](#-0-autenticação)
1. [ResourceNotFoundException (404)](#-1-resourcenotfoundexception-404---not-found)
2. [DomainValidationException (400)](#-2-domainvalidationexception-400---bad-request)
3. [MethodArgumentNotValidException (400)](#-3-methodargumentnotvalidexception-400---validation-error)
4. [Novos Tratamentos de Erro (400, 404, 405, 415)](#-4-novos-tratamentos-de-erro-400-404-405-415)
5. [Casos de Sucesso](#-5-casos-de-sucesso-para-referência)
6. [Checklist de Testes](#-6-checklist-de-testes)
7. [Configuração no Postman](#-configuração-no-postman)
8. [Notas Importantes](#-notas-importantes)

---

## 🔐 0. Autenticação

A API implementa autenticação stateful baseada em HttpSession. A maioria dos endpoints requer autenticação, exceto:
- `POST /auth/login` - Login de usuário
- `POST /v1/users` - Cadastro de novo usuário (público)

**Importante:** Após fazer login, a sessão é mantida automaticamente pelo Postman através de cookies. Para acessar endpoints protegidos, você deve fazer login primeiro.

### 0.1. Login (sucesso)
**Pré-requisito:** Criar um usuário primeiro (ver seção 5.1)

**Método:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "login": "joaosilva",
  "password": "senha123"
}
```

**Resposta Esperada (200):**
```
Status: 200 OK
(Sessão criada automaticamente - cookie JSESSIONID será armazenado)
```

### 0.2. Login (usuário não encontrado)
**Método:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "login": "usuario_inexistente",
  "password": "senha123"
}
```

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with the provided details."
}
```

### 0.3. Login (senha incorreta)
**Pré-requisito:** Ter um usuário criado

**Método:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "login": "joaosilva",
  "password": "senha_errada"
}
```

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with the provided details."
}
```

### 0.4. Login (validação - login vazio)
**Método:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "login": "",
  "password": "senha123"
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "login": "Login is required"
  }
}
```

### 0.5. Login (validação - senha vazia)
**Método:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "login": "joaosilva",
  "password": ""
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "password": "Password is required"
  }
}
```

### 0.6. Logout
**Pré-requisito:** Fazer login primeiro

**Método:** `POST`  
**URL:** `http://localhost:8080/auth/logout`

**Resposta Esperada (200):**
```
Status: 200 OK
(Sessão invalidada)
```

### 0.7. Acesso não autorizado (sem login)
**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users`

**Resposta Esperada (401):**
```json
{
  "type": "https://api.food-backend.com/problems/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Authentication required. Please log in to access this resource."
}
```

### 0.8. Acesso não autorizado (após logout)
**Pré-requisito:** Fazer login e depois logout

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users`

**Resposta Esperada (401):**
```json
{
  "type": "https://api.food-backend.com/problems/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Authentication required. Please log in to access this resource."
}
```

---

## 🔴 1. ResourceNotFoundException (404 - Not Found)

### 1.1. Buscar usuário inexistente por ID
**Pré-requisito:** Fazer login primeiro (ver seção 0.1)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/99999`

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with ID: 99999"
}
```

### 1.2. Atualizar usuário inexistente
**Pré-requisito:** Fazer login primeiro (ver seção 0.1)

**Método:** `PUT`  
**URL:** `http://localhost:8080/v1/users/99999`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "João Silva",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with ID: 99999"
}
```

### 1.3. Deletar usuário inexistente
**Pré-requisito:** Fazer login primeiro (ver seção 0.1)

**Método:** `DELETE`  
**URL:** `http://localhost:8080/v1/users/99999`

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with the provided details."
}
```

### 1.4. Buscar usuário por login inexistente
**Pré-requisito:** Fazer login primeiro (ver seção 0.1)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/login?login=login_inexistente`

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with the provided details."
}
```

### 1.5. Buscar usuário por email inexistente
**Pré-requisito:** Fazer login primeiro (ver seção 0.1)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/email?email=email_inexistente@teste.com`

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with the provided details."
}
```

### 1.6. Alterar senha de usuário inexistente
**Pré-requisito:** Fazer login primeiro (ver seção 0.1)

**Método:** `PATCH`  
**URL:** `http://localhost:8080/v1/users/99999/password`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "currentPassword": "senha123",
  "newPassword": "novasenha456"
}
```

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "User not found with ID: 99999"
}
```

---

## 🟡 2. DomainValidationException (400 - Bad Request)

### 2.1. Criar usuário com email já existente
**Pré-requisito:** Primeiro, crie um usuário válido (ver seção 3.1)

**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "Maria Santos",
  "email": "teste@email.com",
  "login": "mariasantos",
  "password": "senha123",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "456",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```
*(Use o mesmo email do usuário criado anteriormente)*

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/domain-validation-error",
  "title": "Domain Validation Error",
  "status": 400,
  "detail": "The email provided is already registered."
}
```

### 2.2. Alterar senha com senha atual incorreta (quando senha atual = nova senha)
**Pré-requisito:** Tenha um usuário criado (pegar o ID) e fazer login primeiro (ver seção 0.1)

**Método:** `PATCH`  
**URL:** `http://localhost:8080/v1/users/{id}/password`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "currentPassword": "senha123",
  "newPassword": "senha123"
}
```
*(Use a mesma senha para currentPassword e newPassword)*

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/domain-validation-error",
  "title": "Domain Validation Error",
  "status": 400,
  "detail": "The current password provided is incorrect."
}
```

---

## 🟠 3. MethodArgumentNotValidException (400 - Validation Error)

### 3.1. Criar usuário - Campo name vazio
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "",
  "email": "teste@email.com",
  "login": "teste",
  "password": "senha123",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "name": "Name is required"
  }
}
```

### 3.2. Criar usuário - Email inválido
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "João Silva",
  "email": "email-invalido",
  "login": "joaosilva",
  "password": "senha123",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "email": "Invalid email format"
  }
}
```

### 3.3. Criar usuário - Senha muito curta
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "password": "123",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "password": "Password must be at least 6 characters"
  }
}
```

### 3.4. Criar usuário - Campos obrigatórios faltando (múltiplos erros)
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "",
  "email": "",
  "login": "",
  "password": "123",
  "type": null
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "name": "Name is required",
    "email": "Email is required",
    "login": "Login is required",
    "password": "Password must be at least 6 characters",
    "type": "User type is required"
  }
}
```

### 3.5. Criar usuário - Body vazio/null
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "name": "Name is required",
    "email": "Email is required",
    "login": "Login is required",
    "password": "Password is required",
    "type": "User type is required"
  }
}
```

### 3.6. Atualizar usuário - Campo name vazio
**Pré-requisito:** Tenha um usuário criado (pegar o ID) e fazer login primeiro (ver seção 0.1)

**Método:** `PUT`  
**URL:** `http://localhost:8080/v1/users/{id}`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "name": "Name is required"
  }
}
```

### 3.7. Alterar senha - Campos vazios
**Pré-requisito:** Tenha um usuário criado (pegar o ID) e fazer login primeiro (ver seção 0.1)

**Método:** `PATCH`  
**URL:** `http://localhost:8080/v1/users/{id}/password`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "currentPassword": "",
  "newPassword": ""
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "currentPassword": "The current password is required.",
    "newPassword": "The new password is required."
  }
}
```

### 3.8. Alterar senha - Nova senha muito curta
**Pré-requisito:** Tenha um usuário criado (pegar o ID) e fazer login primeiro (ver seção 0.1)

**Método:** `PATCH`  
**URL:** `http://localhost:8080/v1/users/{id}/password`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "currentPassword": "senha123",
  "newPassword": "123"
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/validation-error",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed",
  "errors": {
    "newPassword": "New password must be at least 6 characters"
  }
}
```

---

## 🔵 4. Novos Tratamentos de Erro (400, 404, 405, 415)

### 4.1. HttpMessageNotReadableException (400) - JSON malformado
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "password": "senha123",
  "type": "CUSTOMER"
  "address": {}
}
```
*(JSON malformado - vírgula faltando após "CUSTOMER")*

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/malformed-request",
  "title": "Malformed Request",
  "status": 400,
  "detail": "Request body is malformed or missing. Please check your JSON format."
}
```

### 4.2. HttpMessageNotReadableException (400) - Body vazio quando obrigatório
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:** *(vazio)*

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/malformed-request",
  "title": "Malformed Request",
  "status": 400,
  "detail": "Request body is required but was not provided."
}
```

### 4.3. MissingServletRequestParameterException (400) - Parâmetro name faltando
**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/name`

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/missing-parameter",
  "title": "Missing Required Parameter",
  "status": 400,
  "detail": "Required parameter 'name' is missing",
  "parameter": "name"
}
```

### 4.4. MissingServletRequestParameterException (400) - Parâmetro login faltando
**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/login`

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/missing-parameter",
  "title": "Missing Required Parameter",
  "status": 400,
  "detail": "Required parameter 'login' is missing",
  "parameter": "login"
}
```

### 4.5. MissingServletRequestParameterException (400) - Parâmetro email faltando
**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/email`

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/missing-parameter",
  "title": "Missing Required Parameter",
  "status": 400,
  "detail": "Required parameter 'email' is missing",
  "parameter": "email"
}
```

### 4.6. MethodArgumentTypeMismatchException (400) - ID com tipo inválido (string)
**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/abc`

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/type-mismatch",
  "title": "Type Mismatch",
  "status": 400,
  "detail": "Invalid value 'abc' for parameter 'id'. Expected type: Long",
  "parameter": "id",
  "expectedType": "Long",
  "providedValue": "abc"
}
```

### 4.7. MethodArgumentTypeMismatchException (400) - ID com tipo inválido em PUT
**Método:** `PUT`  
**URL:** `http://localhost:8080/v1/users/xyz`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "João Silva",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Resposta Esperada (400):**
```json
{
  "type": "https://api.food-backend.com/problems/type-mismatch",
  "title": "Type Mismatch",
  "status": 400,
  "detail": "Invalid value 'xyz' for parameter 'id'. Expected type: Long",
  "parameter": "id",
  "expectedType": "Long",
  "providedValue": "xyz"
}
```

### 4.8. HttpRequestMethodNotSupportedException (405) - Método HTTP não suportado
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users/search/name?name=teste`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{}
```

**Resposta Esperada (405):**
```json
{
  "type": "https://api.food-backend.com/problems/method-not-allowed",
  "title": "Method Not Allowed",
  "status": 405,
  "detail": "HTTP method 'POST' is not supported for this endpoint",
  "method": "POST",
  "supportedMethods": ["GET"]
}
```

### 4.9. HttpMediaTypeNotSupportedException (415) - Content-Type XML não suportado
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/xml`  
**Body:**
```xml
<user>
  <name>João</name>
  <email>joao@email.com</email>
</user>
```

**Resposta Esperada (415):**
```json
{
  "type": "https://api.food-backend.com/problems/unsupported-media-type",
  "title": "Unsupported Media Type",
  "status": 415,
  "detail": "Media type 'application/xml' is not supported",
  "contentType": "application/xml",
  "supportedTypes": ["application/json"]
}
```

### 4.10. HttpMediaTypeNotSupportedException (415) - Content-Type text/plain não suportado
**Método:** `PUT`  
**URL:** `http://localhost:8080/v1/users/1`  
**Headers:** `Content-Type: text/plain`  
**Body:**
```
name=João
```

**Resposta Esperada (415):**
```json
{
  "type": "https://api.food-backend.com/problems/unsupported-media-type",
  "title": "Unsupported Media Type",
  "status": 415,
  "detail": "Media type 'text/plain' is not supported",
  "contentType": "text/plain",
  "supportedTypes": ["application/json"]
}
```

### 4.11. NoHandlerFoundException (404) - Endpoint inexistente (GET)
**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/inexistente/rota`

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/endpoint-not-found",
  "title": "Endpoint Not Found",
  "status": 404,
  "detail": "No handler found for GET /v1/users/inexistente/rota",
  "method": "GET",
  "path": "/v1/users/inexistente/rota"
}
```

### 4.12. NoHandlerFoundException (404) - Endpoint inexistente (POST)
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users/rota/que/nao/existe`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{}
```

**Resposta Esperada (404):**
```json
{
  "type": "https://api.food-backend.com/problems/endpoint-not-found",
  "title": "Endpoint Not Found",
  "status": 404,
  "detail": "No handler found for POST /v1/users/rota/que/nao/existe",
  "method": "POST",
  "path": "/v1/users/rota/que/nao/existe"
}
```

---

## ✅ 5. Casos de Sucesso (para referência)

### 5.1. Criar usuário válido (público)
**Método:** `POST`  
**URL:** `http://localhost:8080/v1/users`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "password": "senha123",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  }
}
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  },
  "createdAt": "2024-01-01T10:00:00",
  "lastUpdated": "2024-01-01T10:00:00"
}
```

### 5.2. Login (antes de acessar endpoints protegidos)
**Pré-requisito:** Criar usuário primeiro (ver seção 5.1)

**Método:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "login": "joaosilva",
  "password": "senha123"
}
```

**Resposta Esperada (200):**
```
Status: 200 OK
(Sessão criada - cookie JSESSIONID armazenado)
```

### 5.3. Listar todos os usuários
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users`

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "login": "joaosilva",
    "type": "CUSTOMER",
    "address": {
      "street": "Rua Teste",
      "number": "123",
      "city": "São Paulo",
      "zipCode": "01234-567"
    },
    "createdAt": "2024-01-01T10:00:00",
    "lastUpdated": "2024-01-01T10:00:00"
  }
]
```

### 5.4. Buscar usuário por ID
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/1`

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  },
  "createdAt": "2024-01-01T10:00:00",
  "lastUpdated": "2024-01-01T10:00:00"
}
```

### 5.5. Buscar usuários por nome
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/name?name=João`

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "login": "joaosilva",
    "type": "CUSTOMER",
    "address": {
      "street": "Rua Teste",
      "number": "123",
      "city": "São Paulo",
      "zipCode": "01234-567"
    },
    "createdAt": "2024-01-01T10:00:00",
    "lastUpdated": "2024-01-01T10:00:00"
  }
]
```

### 5.6. Buscar usuário por login
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/login?login=joaosilva`

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  },
  "createdAt": "2024-01-01T10:00:00",
  "lastUpdated": "2024-01-01T10:00:00"
}
```

### 5.7. Buscar usuário por email
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `GET`  
**URL:** `http://localhost:8080/v1/users/search/email?email=joao@email.com`

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Teste",
    "number": "123",
    "city": "São Paulo",
    "zipCode": "01234-567"
  },
  "createdAt": "2024-01-01T10:00:00",
  "lastUpdated": "2024-01-01T10:00:00"
}
```

### 5.8. Atualizar usuário
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `PUT`  
**URL:** `http://localhost:8080/v1/users/1`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "name": "João Silva Atualizado",
  "address": {
    "street": "Rua Nova",
    "number": "456",
    "city": "Rio de Janeiro",
    "zipCode": "20000-000"
  }
}
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "name": "João Silva Atualizado",
  "email": "joao@email.com",
  "login": "joaosilva",
  "type": "CUSTOMER",
  "address": {
    "street": "Rua Nova",
    "number": "456",
    "city": "Rio de Janeiro",
    "zipCode": "20000-000"
  },
  "createdAt": "2024-01-01T10:00:00",
  "lastUpdated": "2024-01-01T11:00:00"
}
```

### 5.9. Alterar senha
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `PATCH`  
**URL:** `http://localhost:8080/v1/users/1/password`  
**Headers:** `Content-Type: application/json`  
**Body:**
```json
{
  "currentPassword": "senha123",
  "newPassword": "novasenha456"
}
```

**Resposta Esperada (204):** *(No Content)*

### 5.10. Deletar usuário
**Pré-requisito:** Fazer login primeiro (ver seção 5.2)

**Método:** `DELETE`  
**URL:** `http://localhost:8080/v1/users/1`

**Resposta Esperada (204):** *(No Content)*

---

## 📋 6. Checklist de Testes

Use esta checklist para garantir que testou todos os cenários:

- [ ] **Autenticação**
  - [ ] Login (sucesso)
  - [ ] Login (usuário não encontrado - 404)
  - [ ] Login (senha incorreta - 404)
  - [ ] Login (validação - login vazio)
  - [ ] Login (validação - senha vazia)
  - [ ] Logout
  - [ ] Acesso não autorizado (sem login - 401)
  - [ ] Acesso não autorizado (após logout - 401)

- [ ] **ResourceNotFoundException (404)**
  - [ ] GET usuário inexistente por ID
  - [ ] GET usuário inexistente por login
  - [ ] GET usuário inexistente por email
  - [ ] PUT usuário inexistente
  - [ ] DELETE usuário inexistente
  - [ ] PATCH senha de usuário inexistente

- [ ] **DomainValidationException (400)**
  - [ ] Criar usuário com email duplicado
  - [ ] Alterar senha com senha atual = nova senha

- [ ] **MethodArgumentNotValidException (400)**
  - [ ] Criar usuário - name vazio
  - [ ] Criar usuário - email inválido
  - [ ] Criar usuário - senha muito curta
  - [ ] Criar usuário - múltiplos campos inválidos
  - [ ] Criar usuário - body vazio
  - [ ] Atualizar usuário - name vazio
  - [ ] Alterar senha - campos vazios
  - [ ] Alterar senha - nova senha muito curta

- [ ] **Novos Tratamentos de Erro**
  - [ ] JSON malformado
  - [ ] Body vazio quando obrigatório
  - [ ] Parâmetro name faltando
  - [ ] Parâmetro login faltando
  - [ ] Parâmetro email faltando
  - [ ] ID com tipo inválido (string) - GET
  - [ ] ID com tipo inválido (string) - PUT
  - [ ] Método HTTP não suportado (POST em GET endpoint)
  - [ ] Content-Type XML não suportado
  - [ ] Content-Type text/plain não suportado
  - [ ] Endpoint inexistente (GET)
  - [ ] Endpoint inexistente (POST)

- [ ] **Casos de Sucesso**
  - [ ] Criar usuário válido (público)
  - [ ] Login (antes de acessar endpoints protegidos)
  - [ ] Listar todos os usuários
  - [ ] Buscar usuário por ID
  - [ ] Buscar usuário por nome
  - [ ] Buscar usuário por login
  - [ ] Buscar usuário por email
  - [ ] Atualizar usuário
  - [ ] Alterar senha
  - [ ] Deletar usuário

---

## 🔧 Configuração no Postman

### Criar Collection
1. Crie uma nova Collection chamada "Food Backend - ProblemDetail Tests"
2. Configure a variável de ambiente:
   - Variable: `base_url`
   - Value: `http://localhost:8080`

### Variáveis de Ambiente
Recomenda-se criar variáveis para facilitar os testes:
- `base_url`: `http://localhost:8080`
- `user_id`: (será preenchido após criar um usuário)

### Headers Padrão
Configure estes headers para todas as requisições que precisam de body:
- `Content-Type`: `application/json`
- `Accept`: `application/json`

---

## 📝 Notas Importantes

1. **Ordem dos Testes**: Recomenda-se criar um usuário primeiro (teste de sucesso) para depois testar os casos de erro relacionados (email duplicado, atualização, etc.)

2. **IDs Dinâmicos**: Após criar um usuário, use o ID retornado nos testes subsequentes

3. **Limpeza**: Para testar "email duplicado", você precisará criar um usuário primeiro. Depois, tente criar outro com o mesmo email

4. **Status Codes**: Verifique sempre o status code HTTP na resposta:
   - 400: Bad Request (validação, erro de domínio, JSON malformado, parâmetro faltando, tipo incorreto)
   - 401: Unauthorized (acesso não autorizado - sessão inválida ou ausente) - retorna ProblemDetail
   - 404: Not Found (recurso não encontrado, endpoint não encontrado)
   - 405: Method Not Allowed (método HTTP não suportado)
   - 415: Unsupported Media Type (Content-Type não suportado)
   - 201: Created (sucesso na criação)
   - 200: OK (sucesso em operações de leitura/atualização)
   - 204: No Content (sucesso em delete)
   - 500: Internal Server Error (erro interno do servidor)

5. **Autenticação**: A maioria dos endpoints requer autenticação. Você deve:
   - Primeiro criar um usuário (POST /v1/users - público)
   - Fazer login (POST /auth/login) para criar uma sessão
   - A sessão é mantida automaticamente pelo Postman através de cookies
   - Para testar endpoints protegidos, sempre faça login primeiro
   - O cadastro de usuário (POST /v1/users) é público e não requer autenticação

6. **Formato de Erro**: Todas as respostas de erro seguem o padrão RFC 7807 (Problem Detail):
   - `type`: URI que identifica o tipo de problema
   - `title`: Título legível do problema
   - `status`: Código HTTP
   - `detail`: Mensagem detalhada do erro
   - `properties`: Propriedades adicionais (quando aplicável)

