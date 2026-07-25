package com.exemplo.apicadastro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiCadastroOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Cadastro")
                        .description("API REST para cadastro de pessoas")
                        .version("v1")
                        .contact(new Contact().name("Eron Moraes").email("eron.moraes7@gmail.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public OpenApiCustomizer contratoPublicado() {
        return openApi -> {
            PathItem colecao = openApi.getPaths().get("/pessoas");
            if (colecao != null && colecao.getPost() != null) {
                colecao.getPost().getResponses().remove("201");
            }

            PathItem recurso = openApi.getPaths().get("/pessoas/{id}");
            if (recurso != null && recurso.getDelete() != null) {
                recurso.getDelete().getResponses().remove("204");
            }

            Schema<?> pessoa = openApi.getComponents().getSchemas().get("Pessoa");
            if (pessoa == null) {
                return;
            }

            Schema<?> cpf = (Schema<?>) pessoa.getProperties().get("cpf");
            if (cpf != null) {
                cpf.setPattern("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
                cpf.setMinLength(14);
            }

            Schema<?> idade = (Schema<?>) pessoa.getProperties().get("idade");
            if (idade != null) {
                idade.setMaximum(BigDecimal.valueOf(120));
            }
        };
    }
}
