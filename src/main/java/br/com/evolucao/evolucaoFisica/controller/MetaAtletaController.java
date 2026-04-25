package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.MetaAtletaRequest;
import br.com.evolucao.evolucaoFisica.dto.MetaAtletaResponse;
import br.com.evolucao.evolucaoFisica.service.MetaAtletaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/metas")
public class MetaAtletaController {

    private final MetaAtletaService metaAtletaService;

    public MetaAtletaController(MetaAtletaService metaAtletaService) {
        this.metaAtletaService = metaAtletaService;
    }

    @GetMapping
    public MetaAtletaResponse buscar(@PathVariable Long usuarioId) {
        return metaAtletaService.buscarPorUsuario(usuarioId);
    }

    @PutMapping
    public MetaAtletaResponse salvar(@PathVariable Long usuarioId, @Valid @RequestBody MetaAtletaRequest request) {
        return metaAtletaService.salvar(usuarioId, request);
    }
}
