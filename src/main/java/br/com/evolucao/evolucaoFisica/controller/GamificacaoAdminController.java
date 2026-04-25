package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.MedalhaRequest;
import br.com.evolucao.evolucaoFisica.dto.MedalhaResponse;
import br.com.evolucao.evolucaoFisica.dto.MissaoSemanalRequest;
import br.com.evolucao.evolucaoFisica.dto.MissaoSemanalResponse;
import br.com.evolucao.evolucaoFisica.dto.XpRegraRequest;
import br.com.evolucao.evolucaoFisica.dto.XpRegraResponse;
import br.com.evolucao.evolucaoFisica.service.GamificacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/gamificacao")
public class GamificacaoAdminController {

    private final GamificacaoService gamificacaoService;

    public GamificacaoAdminController(GamificacaoService gamificacaoService) {
        this.gamificacaoService = gamificacaoService;
    }

    @PostMapping("/xp-regras")
    @ResponseStatus(HttpStatus.CREATED)
    public XpRegraResponse criarXpRegra(@Valid @RequestBody XpRegraRequest request) {
        return gamificacaoService.salvarXpRegra(null, request);
    }

    @PutMapping("/xp-regras/{id}")
    public XpRegraResponse atualizarXpRegra(@PathVariable Long id, @Valid @RequestBody XpRegraRequest request) {
        return gamificacaoService.salvarXpRegra(id, request);
    }

    @GetMapping("/xp-regras")
    public List<XpRegraResponse> listarXpRegras() {
        return gamificacaoService.listarXpRegras();
    }

    @PostMapping("/medalhas")
    @ResponseStatus(HttpStatus.CREATED)
    public MedalhaResponse criarMedalha(@Valid @RequestBody MedalhaRequest request) {
        return gamificacaoService.salvarMedalha(null, request);
    }

    @PutMapping("/medalhas/{id}")
    public MedalhaResponse atualizarMedalha(@PathVariable Long id, @Valid @RequestBody MedalhaRequest request) {
        return gamificacaoService.salvarMedalha(id, request);
    }

    @GetMapping("/medalhas")
    public List<MedalhaResponse> listarMedalhas() {
        return gamificacaoService.listarMedalhasConfiguradas();
    }

    @PostMapping("/missoes-semanais")
    @ResponseStatus(HttpStatus.CREATED)
    public MissaoSemanalResponse criarMissao(@Valid @RequestBody MissaoSemanalRequest request) {
        return gamificacaoService.salvarMissao(null, request);
    }

    @PutMapping("/missoes-semanais/{id}")
    public MissaoSemanalResponse atualizarMissao(@PathVariable Long id, @Valid @RequestBody MissaoSemanalRequest request) {
        return gamificacaoService.salvarMissao(id, request);
    }

    @GetMapping("/missoes-semanais")
    public List<MissaoSemanalResponse> listarMissoes() {
        return gamificacaoService.listarMissoesConfiguradas();
    }
}
