package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.AlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.AlimentoResponse;
import br.com.evolucao.evolucaoFisica.service.AlimentoService;
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

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoService alimentoService;

    public AlimentoController(AlimentoService alimentoService) {
        this.alimentoService = alimentoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlimentoResponse criar(@Valid @RequestBody AlimentoRequest request) {
        return alimentoService.criar(request);
    }

    @GetMapping
    public List<AlimentoResponse> listar() {
        return alimentoService.listar();
    }

    @GetMapping("/{id}")
    public AlimentoResponse buscarPorId(@PathVariable Long id) {
        return alimentoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AlimentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlimentoRequest request) {
        return alimentoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        alimentoService.excluir(id);
    }
}
