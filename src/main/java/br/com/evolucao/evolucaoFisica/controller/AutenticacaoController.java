package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.AuthResponse;
import br.com.evolucao.evolucaoFisica.dto.CadastroLocalRequest;
import br.com.evolucao.evolucaoFisica.dto.LoginGoogleRequest;
import br.com.evolucao.evolucaoFisica.dto.LoginLocalRequest;
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
@RequestMapping("/api")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/auth/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse cadastrarLocal(@Valid @RequestBody CadastroLocalRequest request) {
        return autenticacaoService.cadastrarLocal(request);
    }

    @PostMapping("/auth/login")
    public AuthResponse loginLocal(@Valid @RequestBody LoginLocalRequest request) {
        return autenticacaoService.loginLocal(request);
    }

    @PostMapping("/auth/google")
    public AuthResponse loginGoogle(@Valid @RequestBody LoginGoogleRequest request) {
        return autenticacaoService.loginGoogle(request);
    }

    @PostMapping("/autenticacao/identidades")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioIdentidadeExternaResponse vincular(@Valid @RequestBody UsuarioIdentidadeExternaRequest request) {
        return autenticacaoService.vincularIdentidadeExterna(request);
    }

    @GetMapping("/autenticacao/identidades/usuarios/{usuarioId}")
    public List<UsuarioIdentidadeExternaResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return autenticacaoService.listarIdentidadesPorUsuario(usuarioId);
    }

    @GetMapping("/autenticacao/identidades/provedores/{provedor}/externos/{identificadorExterno}")
    public UsuarioIdentidadeExternaResponse buscarPorIdentidade(
            @PathVariable ProvedorAutenticacao provedor,
            @PathVariable String identificadorExterno
    ) {
        return autenticacaoService.buscarPorProvedorEIdentificador(identificadorExterno, provedor);
    }
}
