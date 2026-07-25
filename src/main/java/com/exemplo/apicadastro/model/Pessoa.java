package com.exemplo.apicadastro.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Schema(
        name = "Pessoa",
        description = "Pessoa cadastrada no sistema",
        example = """
                {
                  "id": 1,
                  "cpf": "123.456.789-00",
                  "nome": "Maria Silva",
                  "email": "maria.silva@exemplo.com"
                }"""
)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador da pessoa", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "CPF e obrigatorio")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 digitos numericos")
    @Column(unique = true, nullable = false, length = 11)
    @Schema(description = "CPF da pessoa, com pontuacao", example = "123.456.789-00",
            maxLength = 14, requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpf;

    @NotBlank(message = "Nome e obrigatorio")
    @Column(nullable = false)
    @Schema(description = "Nome completo da pessoa", example = "Maria Silva",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotNull(message = "Idade e obrigatoria")
    @Min(value = 0, message = "Idade nao pode ser negativa")
    @Max(value = 150, message = "Idade nao pode ser maior que 150")
    @Column(nullable = false)
    @Schema(description = "Idade da pessoa em anos", example = "30",
            minimum = "0", maximum = "120", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer idade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }
}
