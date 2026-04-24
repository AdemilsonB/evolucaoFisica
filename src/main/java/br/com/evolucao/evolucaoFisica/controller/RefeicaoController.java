package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.RefeicaoAlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoAlimentoResponse;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoRequest;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoResponse;
import br.com.evolucao.evolucaoFisica.service.AlimentacaoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/refeicoes")
public class RefeicaoController {

    private final AlimentacaoService alimentacaoService;

    public RefeicaoController(AlimentacaoService alimentacaoService) {
        this.alimentacaoService = alimentacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RefeicaoResponse criar(@Valid @RequestBody RefeicaoRequest request) {
        return alimentacaoService.registrarRefeicao(request);
    }

    @PostMapping("/{id}/alimentos")
    @ResponseStatus(HttpStatus.CREATED)
    public RefeicaoAlimentoResponse adicionarAlimento(
            @PathVariable Long id,
            @Valid @RequestBody RefeicaoAlimentoRequest request
    ) {
        return alimentacaoService.adicionarAlimento(id, request);
    }

    @GetMapping
    public List<RefeicaoResponse> listar(
            @RequestParam Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim
    ) {
        return alimentacaoService.listarRefeicoes(usuarioId, dataInicio, dataFim);
    }

    @GetMapping("/{id}")
    public RefeicaoResponse buscarPorId(@PathVariable Long id) {
        return alimentacaoService.buscarPorId(id);
    }
}
