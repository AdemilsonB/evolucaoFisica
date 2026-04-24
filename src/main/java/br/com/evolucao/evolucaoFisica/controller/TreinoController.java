package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.TreinoExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.TreinoExercicioResponse;
import br.com.evolucao.evolucaoFisica.dto.TreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.TreinoResponse;
import br.com.evolucao.evolucaoFisica.service.TreinoService;
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
@RequestMapping("/api/treinos")
public class TreinoController {

    private final TreinoService treinoService;

    public TreinoController(TreinoService treinoService) {
        this.treinoService = treinoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TreinoResponse criar(@Valid @RequestBody TreinoRequest request) {
        return treinoService.criarTreino(request);
    }

    @GetMapping
    public List<TreinoResponse> listarPorUsuario(@RequestParam Long usuarioId) {
        return treinoService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public TreinoResponse buscarPorId(@PathVariable Long id) {
        return treinoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public TreinoResponse atualizar(@PathVariable Long id, @Valid @RequestBody TreinoRequest request) {
        return treinoService.atualizar(id, request);
    }

    @PostMapping("/{id}/exercicios")
    @ResponseStatus(HttpStatus.CREATED)
    public TreinoExercicioResponse adicionarExercicio(
            @PathVariable Long id,
            @Valid @RequestBody TreinoExercicioRequest request
    ) {
        return treinoService.adicionarExercicio(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        treinoService.excluir(id);
    }
}
