package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.FinalizacaoRegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroExercicioResponse;
import br.com.evolucao.evolucaoFisica.dto.RegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroTreinoResponse;
import br.com.evolucao.evolucaoFisica.service.RegistroTreinoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/registros-treino")
public class RegistroTreinoController {

    private final RegistroTreinoService registroTreinoService;

    public RegistroTreinoController(RegistroTreinoService registroTreinoService) {
        this.registroTreinoService = registroTreinoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroTreinoResponse iniciar(@Valid @RequestBody RegistroTreinoRequest request) {
        return registroTreinoService.iniciarTreino(request);
    }

    @PostMapping("/{id}/execucoes")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistroExercicioResponse registrarExecucao(
            @PathVariable Long id,
            @Valid @RequestBody RegistroExercicioRequest request
    ) {
        return registroTreinoService.registrarExecucao(id, request);
    }

    @PutMapping("/{id}/finalizacao")
    public RegistroTreinoResponse finalizar(
            @PathVariable Long id,
            @Valid @RequestBody FinalizacaoRegistroTreinoRequest request
    ) {
        return registroTreinoService.finalizarTreino(id, request);
    }

    @GetMapping
    public List<RegistroTreinoResponse> listar(
            @RequestParam Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim
    ) {
        return registroTreinoService.listar(usuarioId, dataInicio, dataFim);
    }

    @GetMapping("/{id}")
    public RegistroTreinoResponse buscarPorId(@PathVariable Long id) {
        return registroTreinoService.buscarPorId(id);
    }
}
