# mobiauto-backend-interview

Sistema de gerenciamento de revendas de veículos desenvolvido para atender os requisitos presentes no desafio de backend da Mobiauto conforme documentação presente em `docs/Senior_Backend_Developers_Interview.pdf`.

Esse sistema foi desenvolvido utilizando as versões Java 17 e Spring Boot 3.

## Pré-requisitos

- Java 17
- Maven
- Docker (opcional)

## Banco de dados
A fim de facilitar a execução do projeto, foi utilizado um banco de dados em memória H2. O banco de dados é populado com dados iniciais ao iniciar a aplicação.

![img_3.png](docs/execute-on-init.png)

Para visualizar o H2, basta acessar o endereço a seguir - as credenciais de acesso estão no arquivo `application.yml`:
```
http://localhost:8080/h2-console
```

![img.png](docs/h2.png)


## Dependências

- **spring-boot-starter-data-jpa**: utilizado para persistência de dados
- **spring-boot-starter-web**: utilizado para criação de APIs REST
- **spring-boot-starter-security**: utilizado para segurança da aplicação
- **spring-boot-starter-validation**: utilizado para validação de dados
- **h2**: utilizado para banco de dados em memória
- **spring-boot-starter-test**: utilizado para testes unitários
- **spring-boot-security-test**: utilizado para testes de segurança
- **springdoc-openapi-starter-webmvc-ui**: utilizado para documentação da API
- **jjwt-api**: utilizado para geração de tokens JWT
- **jjwt-impl**: utilizado para geração de tokens JWT
- **jjwt-gson**: utilizado para geração de tokens JWT

## Execução

### Utilizando Maven
1. Clone o repositório
2. Execute o comando `cd mobiauto-backend-interview`
3. Execute o comando `mvn spring-boot:run` na raiz do projeto

### Utilizando Docker

1. Clone o repositório
2. Execute o comando `cd mobiauto-backend-interview`
3. Execute o comando `mvn clean package` na raiz do projeto
4. Execute o comando `docker compose up --build -d` e o serviço estará rodando na porta 8080

### Utilizando IDE sa sua escolha
Rode a aplicação utilizando uma IDE da sua escolha. O sistema estará rodando na porta 8080.

## Documentação da API

### Swagger

Estando o sistema rodando na porta 8080. Acesse o endereço a seguir para visualizar o Swagger da API.
```
http://localhost:8080/swagger-ui.html
```
![img.png](docs/swagger.png)

### Postman

Importe o arquivo `postman/mobiauto.postman_collection.json` para acessar a collection Postman e utilizar as requisições disponíveis.

## Autenticação

Nessa aplciação a autenticação se dá por meio de tokens JWT. Para obter um token, basta realizar uma requisição POST para o endpoint `/api/v1/usuario/login` com o corpo da requisição contendo o email e senha do usuário. Exemplo:

### Request
```json
{
  "email": "email@email.com",
  "senha": "123456"
}
```
### Response
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpYXQiOjE3MTk0ODk2NTYsImV4cCI6MTcxOTU3NjA1Nn0.SGY5WLT8R0lxOLsOdeYTBIgYc9WZ_yfCjzdzRQ5KiYXVVpI3KpLTV7XkZ7CNxPfl"
}
```
### Demais requisições
Sendo assim, as demais requisições devem conter o token JWT no cabeçalho `Authorization` para serem autenticadas. Exemplo:
```
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJpYXQiOjE3MTk0ODk2NTYsImV4cCI6MTcxOTU3NjA1Nn0.SGY5WLT8R0lxOLsOdeYTBIgYc9WZ_yfCjzdzRQ5KiYXVVpI3KpLTV7XkZ7CNxPfl
```

## Testes

Os testes unitários da aplicação foram realizados com o uso **Junit 5** e **Mockito**. Para executar os testes, execute o comando `mvn test` na raiz do projeto.

O sistema encontra-se com cobertura de 100% em todas as classes que implementam regras de negócio.

![img_2.png](docs/coverage.png)