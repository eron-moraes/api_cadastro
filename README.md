# API Cadastro

API REST para cadastro de pessoas, construída com Spring Boot 3.5 e banco H2 em memória.

## Requisitos

- Java 17 ou superior
- Maven 3.6+

## Como rodar

Em desenvolvimento:

```bash
mvn spring-boot:run
```

Build do jar executável e execução:

```bash
mvn clean package
java -jar target/api-cadastro-0.0.1-SNAPSHOT.jar
```

A aplicação sobe em `http://localhost:8080`.

## Comandos Maven úteis

| Comando | Descrição |
|---|---|
| `mvn compile` | Compila as classes em `target/classes` |
| `mvn clean` | Limpa o diretório `target/` |
| `mvn package` | Gera o jar executável |
| `mvn package -DskipTests` | Gera o jar pulando os testes |
| `mvn spring-boot:run` | Sobe a aplicação sem empacotar |

## Documentação da API

Com a aplicação no ar:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI (JSON)**: http://localhost:8080/v3/api-docs

Os endpoints ficam sob o recurso `/pessoas`. Consulte o Swagger UI para os
contratos de requisição e resposta.

## Banco de dados

H2 em memória — os dados são perdidos a cada reinicialização.

- **Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:cadastrodb`
- **Usuário**: `sa`
- **Senha**: (em branco)

## Testes manuais

A collection do Postman está em [`postman/ApiCadastro.postman_collection.json`](postman/ApiCadastro.postman_collection.json).

## Estrutura do projeto

```
src/main/java/com/exemplo/apicadastro/
├── ApiCadastroApplication.java   # classe main
├── config/OpenApiConfig.java     # configuração do Swagger/OpenAPI
├── controller/PessoaController.java
├── exception/                    # ApiError + GlobalExceptionHandler
├── model/Pessoa.java             # entidade JPA
└── repository/PessoaRepository.java
```

## Licença

MIT — veja [LICENSE](LICENSE).
