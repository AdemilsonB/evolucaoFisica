package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.EvolucaoFisicaRequest;
import br.com.evolucao.evolucaoFisica.dto.EvolucaoFisicaResponse;
import br.com.evolucao.evolucaoFisica.service.EvolucaoFisicaService;
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
@RequestMapping("/api/evolucoes-fisicas")
public class EvolucaoFisicaController {

    private final EvolucaoFisicaService evolucaoFisicaService;

    public EvolucaoFisicaController(EvolucaoFisicaService evolucaoFisicaService) {
        this.evolucaoFisicaService = evolucaoFisicaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvolucaoFisicaResponse criar(@Valid @RequestBody EvolucaoFisicaRequest request) {
        return evolucaoFisicaService.registrar(request);
    }

    @GetMapping
    public List<EvolucaoFisicaResponse> listar(@RequestParam Long usuarioId) {
        return evolucaoFisicaService.listarHistorico(usuarioId);
    }

    @GetMapping("/{id}")
    public EvolucaoFisicaResponse buscarPorId(@PathVariable Long id) {
        return evolucaoFisicaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public EvolucaoFisicaResponse atualizar(@PathVariable Long id, @Valid @RequestBody EvolucaoFisicaRequest request) {
        return evolucaoFisicaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        evolucaoFisicaService.excluir(id);
    }
}
