package com.exemplo.apicadastro.controller;

import com.exemplo.apicadastro.model.Pessoa;
import com.exemplo.apicadastro.repository.PessoaRepository;
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
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa criar(@Valid @RequestBody Pessoa pessoa) {
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF ja cadastrado");
        }
        return pessoaRepository.save(pessoa);
    }

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pessoa buscarPorId(@PathVariable Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa nao encontrada"));
    }

    @PutMapping("/{id}")
    public Pessoa atualizar(@PathVariable Long id, @Valid @RequestBody Pessoa pessoaAtualizada) {
        Pessoa pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa nao encontrada"));

        boolean cpfEmUsoPorOutraPessoa = pessoaRepository.existsByCpf(pessoaAtualizada.getCpf())
                && !pessoaExistente.getCpf().equals(pessoaAtualizada.getCpf());
        if (cpfEmUsoPorOutraPessoa) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF ja cadastrado");
        }

        pessoaExistente.setCpf(pessoaAtualizada.getCpf());
        pessoaExistente.setNome(pessoaAtualizada.getNome());
        return pessoaRepository.save(pessoaExistente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa nao encontrada");
        }
        pessoaRepository.deleteById(id);
    }
}
