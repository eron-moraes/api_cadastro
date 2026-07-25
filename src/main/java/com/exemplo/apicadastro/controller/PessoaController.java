package com.exemplo.apicadastro.controller;

import com.exemplo.apicadastro.exception.ApiError;
import com.exemplo.apicadastro.model.Pessoa;
import com.exemplo.apicadastro.repository.PessoaRepository;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
@Tag(name = "Pessoas", description = "Cadastro de pessoas")
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma nova pessoa",
            description = "Cria um registro de pessoa a partir do CPF, nome e idade informados.")
    @ApiResponse(responseCode = "200", description = "Pessoa cadastrada com sucesso",
            content = @Content(schema = @Schema(implementation = Pessoa.class)))
    @ApiResponse(responseCode = "400", description = "Dados invalidos",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Pessoa nao encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public Pessoa criar(@Valid @RequestBody Pessoa pessoa) {
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF ja cadastrado");
        }
        return pessoaRepository.save(pessoa);
    }

    @GetMapping
    @Operation(summary = "Lista as pessoas cadastradas",
            description = "Retorna todas as pessoas, com filtro opcional por nome.")
    @Parameter(name = "nome", in = ParameterIn.QUERY, required = false,
            description = "Filtra as pessoas cujo nome contenha o texto informado",
            schema = @Schema(type = "string", example = "Maria"))
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @GetMapping("/{id}")
    @Hidden
    public Pessoa buscarPorId(@PathVariable Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa nao encontrada"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma pessoa existente",
            description = "Substitui os dados da pessoa identificada pelo id.")
    @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = Pessoa.class)))
    @ApiResponse(responseCode = "400", description = "Dados invalidos",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Pessoa nao encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "CPF ja cadastrado para outra pessoa",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public Pessoa atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoaAtualizada) {
        Pessoa pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa nao encontrada"));

        if (pessoaRepository.existsByCpfAndIdNot(pessoaAtualizada.getCpf(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF ja cadastrado");
        }

        pessoaExistente.setCpf(pessoaAtualizada.getCpf());
        pessoaExistente.setNome(pessoaAtualizada.getNome());
        pessoaExistente.setIdade(pessoaAtualizada.getIdade());
        return pessoaRepository.save(pessoaExistente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove uma pessoa",
            description = "Exclui o cadastro da pessoa e devolve o registro removido.")
    @ApiResponse(responseCode = "200", description = "Pessoa removida com sucesso",
            content = @Content(schema = @Schema(implementation = Pessoa.class)))
    @ApiResponse(responseCode = "404", description = "Pessoa nao encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public void remover(@PathVariable Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa nao encontrada");
        }
        pessoaRepository.deleteById(id);
    }
}
