package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarDiaRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarDiaResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoAlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoAlimentoResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarResponse;
import br.com.evolucao.evolucaoFisica.service.PlanoAlimentarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/planos-alimentares")
public class PlanoAlimentarController {

    private final PlanoAlimentarService planoAlimentarService;

    public PlanoAlimentarController(PlanoAlimentarService planoAlimentarService) {
        this.planoAlimentarService = planoAlimentarService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoAlimentarResponse criar(@Valid @RequestBody PlanoAlimentarRequest request) {
        return planoAlimentarService.criarPlano(request);
    }

    @GetMapping
    public List<PlanoAlimentarResponse> listar(@RequestParam Long usuarioId) {
        return planoAlimentarService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/dia-da-semana")
    public List<PlanoAlimentarDiaResponse> listarDiaDaSemana(@RequestParam Long usuarioId, @RequestParam DayOfWeek diaSemana) {
        return planoAlimentarService.listarDiaDaSemana(usuarioId, diaSemana);
    }

    @PostMapping("/{id}/dias")
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoAlimentarDiaResponse adicionarDia(@PathVariable Long id, @Valid @RequestBody PlanoAlimentarDiaRequest request) {
        return planoAlimentarService.adicionarDia(id, request);
    }

    @PostMapping("/dias/{planoDiaId}/refeicoes")
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoAlimentarRefeicaoResponse adicionarRefeicao(
            @PathVariable Long planoDiaId,
            @Valid @RequestBody PlanoAlimentarRefeicaoRequest request
    ) {
        return planoAlimentarService.adicionarRefeicao(planoDiaId, request);
    }

    @PostMapping("/refeicoes/{planoRefeicaoId}/alimentos")
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoAlimentarRefeicaoAlimentoResponse adicionarAlimento(
            @PathVariable Long planoRefeicaoId,
            @Valid @RequestBody PlanoAlimentarRefeicaoAlimentoRequest request
    ) {
        return planoAlimentarService.adicionarAlimento(planoRefeicaoId, request);
    }
}
