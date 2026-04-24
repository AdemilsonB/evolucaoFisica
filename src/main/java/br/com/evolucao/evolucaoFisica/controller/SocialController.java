package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.GrupoMembroRequest;
import br.com.evolucao.evolucaoFisica.dto.GrupoMembroResponse;
import br.com.evolucao.evolucaoFisica.dto.GrupoRequest;
import br.com.evolucao.evolucaoFisica.dto.GrupoResponse;
import br.com.evolucao.evolucaoFisica.dto.PostagemComentarioRequest;
import br.com.evolucao.evolucaoFisica.dto.PostagemComentarioResponse;
import br.com.evolucao.evolucaoFisica.dto.PostagemRequest;
import br.com.evolucao.evolucaoFisica.dto.PostagemResponse;
import br.com.evolucao.evolucaoFisica.dto.SeguidorResponse;
import br.com.evolucao.evolucaoFisica.service.SocialService;
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
@RequestMapping("/api/social")
public class SocialController {

    private final SocialService socialService;

    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    @PostMapping("/usuarios/{seguidorId}/seguir/{seguidoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SeguidorResponse seguir(@PathVariable Long seguidorId, @PathVariable Long seguidoId) {
        return socialService.seguir(seguidorId, seguidoId);
    }

    @GetMapping("/usuarios/{usuarioId}/seguidores")
    public List<SeguidorResponse> listarSeguidores(@PathVariable Long usuarioId) {
        return socialService.listarSeguidores(usuarioId);
    }

    @GetMapping("/usuarios/{usuarioId}/seguindo")
    public List<SeguidorResponse> listarSeguindo(@PathVariable Long usuarioId) {
        return socialService.listarSeguindo(usuarioId);
    }

    @PostMapping("/grupos")
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoResponse criarGrupo(@Valid @RequestBody GrupoRequest request) {
        return socialService.criarGrupo(request);
    }

    @GetMapping("/grupos")
    public List<GrupoResponse> listarGrupos() {
        return socialService.listarGrupos();
    }

    @PostMapping("/grupos/{grupoId}/membros")
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoMembroResponse adicionarMembro(@PathVariable Long grupoId, @Valid @RequestBody GrupoMembroRequest request) {
        return socialService.adicionarMembro(grupoId, request);
    }

    @PostMapping("/postagens")
    @ResponseStatus(HttpStatus.CREATED)
    public PostagemResponse criarPostagem(@Valid @RequestBody PostagemRequest request) {
        return socialService.criarPostagem(request);
    }

    @GetMapping("/postagens/feed")
    public List<PostagemResponse> feed(@RequestParam Long usuarioId) {
        return socialService.listarFeed(usuarioId);
    }

    @GetMapping("/usuarios/{usuarioId}/postagens")
    public List<PostagemResponse> listarPostagensUsuario(@PathVariable Long usuarioId) {
        return socialService.listarPostagensUsuario(usuarioId);
    }

    @PostMapping("/postagens/{postagemId}/curtidas")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void curtir(@PathVariable Long postagemId, @RequestParam Long usuarioId) {
        socialService.curtirPostagem(postagemId, usuarioId);
    }

    @PostMapping("/postagens/{postagemId}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public PostagemComentarioResponse comentar(
            @PathVariable Long postagemId,
            @Valid @RequestBody PostagemComentarioRequest request
    ) {
        return socialService.comentar(postagemId, request);
    }
}
