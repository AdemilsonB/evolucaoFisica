package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.ExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.ExercicioResponse;
import br.com.evolucao.evolucaoFisica.service.ExercicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExercicioResponse criar(@Valid @RequestBody ExercicioRequest request) {
        return exercicioService.criar(request);
    }

    @GetMapping
    public List<ExercicioResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String grupoMuscular,
            @RequestParam(required = false) String equipamento,
            @RequestParam(required = false) Boolean ativo
    ) {
        if (nome != null || grupoMuscular != null || equipamento != null || ativo != null) {
            return exercicioService.buscar(nome, grupoMuscular, equipamento, ativo);
        }
        return exercicioService.listar();
    }

    @GetMapping("/{id}")
    public ExercicioResponse buscarPorId(@PathVariable Long id) {
        return exercicioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ExercicioResponse atualizar(@PathVariable Long id, @Valid @RequestBody ExercicioRequest request) {
        return exercicioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        exercicioService.excluir(id);
    }
}
