package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.UsuarioIdentidadeExternaRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioIdentidadeExternaResponse;
import br.com.evolucao.evolucaoFisica.enumeration.ProvedorAutenticacao;
import br.com.evolucao.evolucaoFisica.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/autenticacao/identidades")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioIdentidadeExternaResponse vincular(@Valid @RequestBody UsuarioIdentidadeExternaRequest request) {
        return autenticacaoService.vincularIdentidadeExterna(request);
    }

    @GetMapping("/usuarios/{usuarioId}")
    public List<UsuarioIdentidadeExternaResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return autenticacaoService.listarIdentidadesPorUsuario(usuarioId);
    }

    @GetMapping("/provedores/{provedor}/externos/{identificadorExterno}")
    public UsuarioIdentidadeExternaResponse buscarPorIdentidade(
            @PathVariable ProvedorAutenticacao provedor,
            @PathVariable String identificadorExterno
    ) {
        return autenticacaoService.buscarPorProvedorEIdentificador(identificadorExterno, provedor);
    }
}
