package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.DashboardGamificacaoResponse;
import br.com.evolucao.evolucaoFisica.dto.RankingItemResponse;
import br.com.evolucao.evolucaoFisica.dto.RegistroDiarioRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroDiarioResponse;
import br.com.evolucao.evolucaoFisica.service.GamificacaoService;
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

import java.util.List;

@RestController
@RequestMapping("/api/gamificacao")
public class GamificacaoController {

    private final GamificacaoService gamificacaoService;

    public GamificacaoController(GamificacaoService gamificacaoService) {
        this.gamificacaoService = gamificacaoService;
    }

    @PostMapping("/registros-diarios")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroDiarioResponse registrarDia(@Valid @RequestBody RegistroDiarioRequest request) {
        return gamificacaoService.registrarDia(request);
    }

    @GetMapping("/registros-diarios")
    public List<RegistroDiarioResponse> listarRegistros(@RequestParam Long usuarioId) {
        return gamificacaoService.listarRegistrosDiarios(usuarioId);
    }

    @GetMapping("/dashboard/{usuarioId}")
    public DashboardGamificacaoResponse dashboard(@PathVariable Long usuarioId) {
        return gamificacaoService.montarDashboard(usuarioId);
    }

    @GetMapping("/ranking/geral")
    public List<RankingItemResponse> rankingGeral() {
        return gamificacaoService.rankingGeral();
    }

    @GetMapping("/ranking/seguindo/{usuarioId}")
    public List<RankingItemResponse> rankingSeguindo(@PathVariable Long usuarioId) {
        return gamificacaoService.rankingSeguindo(usuarioId);
    }

    @GetMapping("/ranking/grupo/{grupoId}")
    public List<RankingItemResponse> rankingGrupo(@PathVariable Long grupoId) {
        return gamificacaoService.rankingGrupo(grupoId);
    }
}
