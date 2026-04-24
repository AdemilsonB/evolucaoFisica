package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.AtualizarPesoRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioResponse;
import br.com.evolucao.evolucaoFisica.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.criarUsuario(request);
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.atualizar(id, request);
    }

    @PatchMapping("/{id}/peso")
    public UsuarioResponse atualizarPeso(@PathVariable Long id, @Valid @RequestBody AtualizarPesoRequest request) {
        return usuarioService.atualizarPeso(id, request);
    }
}
