package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.GrupoMembroRequest;
import br.com.evolucao.evolucaoFisica.dto.GrupoMembroResponse;
import br.com.evolucao.evolucaoFisica.dto.GrupoRequest;
import br.com.evolucao.evolucaoFisica.dto.GrupoResponse;
import br.com.evolucao.evolucaoFisica.dto.PostagemComentarioRequest;
import br.com.evolucao.evolucaoFisica.dto.PostagemComentarioResponse;
import br.com.evolucao.evolucaoFisica.dto.PostagemRequest;
import br.com.evolucao.evolucaoFisica.dto.PostagemResponse;
import br.com.evolucao.evolucaoFisica.dto.SeguidorResponse;
import br.com.evolucao.evolucaoFisica.entity.Grupo;
import br.com.evolucao.evolucaoFisica.entity.GrupoMembro;
import br.com.evolucao.evolucaoFisica.entity.Postagem;
import br.com.evolucao.evolucaoFisica.entity.PostagemComentario;
import br.com.evolucao.evolucaoFisica.entity.PostagemCurtida;
import br.com.evolucao.evolucaoFisica.entity.Seguidor;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.GrupoMembroRepository;
import br.com.evolucao.evolucaoFisica.repository.GrupoRepository;
import br.com.evolucao.evolucaoFisica.repository.PostagemComentarioRepository;
import br.com.evolucao.evolucaoFisica.repository.PostagemCurtidaRepository;
import br.com.evolucao.evolucaoFisica.repository.PostagemRepository;
import br.com.evolucao.evolucaoFisica.repository.SeguidorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class SocialService {

    private final SeguidorRepository seguidorRepository;
    private final GrupoRepository grupoRepository;
    private final GrupoMembroRepository grupoMembroRepository;
    private final PostagemRepository postagemRepository;
    private final PostagemCurtidaRepository postagemCurtidaRepository;
    private final PostagemComentarioRepository postagemComentarioRepository;
    private final UsuarioService usuarioService;
    private final RegistroTreinoService registroTreinoService;
    private final EvolucaoFisicaService evolucaoFisicaService;

    public SocialService(
            SeguidorRepository seguidorRepository,
            GrupoRepository grupoRepository,
            GrupoMembroRepository grupoMembroRepository,
            PostagemRepository postagemRepository,
            PostagemCurtidaRepository postagemCurtidaRepository,
            PostagemComentarioRepository postagemComentarioRepository,
            UsuarioService usuarioService,
            RegistroTreinoService registroTreinoService,
            EvolucaoFisicaService evolucaoFisicaService
    ) {
        this.seguidorRepository = seguidorRepository;
        this.grupoRepository = grupoRepository;
        this.grupoMembroRepository = grupoMembroRepository;
        this.postagemRepository = postagemRepository;
        this.postagemCurtidaRepository = postagemCurtidaRepository;
        this.postagemComentarioRepository = postagemComentarioRepository;
        this.usuarioService = usuarioService;
        this.registroTreinoService = registroTreinoService;
        this.evolucaoFisicaService = evolucaoFisicaService;
    }

    @Transactional
    public SeguidorResponse seguir(Long seguidorId, Long seguidoId) {
        if (seguidorId.equals(seguidoId)) {
            throw new BusinessException("Usuario nao pode seguir a si mesmo.");
        }

        Usuario seguidorUsuario = usuarioService.buscarEntidade(seguidorId);
        Usuario seguidoUsuario = usuarioService.buscarEntidade(seguidoId);

        Seguidor seguidor = seguidorRepository.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId).orElseGet(Seguidor::new);
        seguidor.setSeguidor(seguidorUsuario);
        seguidor.setSeguido(seguidoUsuario);
        seguidor.setAtivo(Boolean.TRUE);

        return toResponse(seguidorRepository.save(seguidor));
    }

    public List<SeguidorResponse> listarSeguidores(Long usuarioId) {
        return seguidorRepository.findAllBySeguidoIdAndAtivoTrue(usuarioId).stream().map(this::toResponse).toList();
    }

    public List<SeguidorResponse> listarSeguindo(Long usuarioId) {
        return seguidorRepository.findAllBySeguidorIdAndAtivoTrue(usuarioId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public GrupoResponse criarGrupo(GrupoRequest request) {
        Grupo grupo = new Grupo();
        grupo.setNome(request.nome());
        grupo.setDescricao(request.descricao());
        grupo.setCriador(usuarioService.buscarEntidade(request.criadorId()));
        grupo.setVisibilidade(request.visibilidade());
        grupo.setAtivo(Boolean.TRUE);
        Grupo salvo = grupoRepository.save(grupo);

        GrupoMembro dono = new GrupoMembro();
        dono.setGrupo(salvo);
        dono.setUsuario(grupo.getCriador());
        dono.setRole(br.com.evolucao.evolucaoFisica.enumeration.RoleGrupo.DONO);
        dono.setAtivo(Boolean.TRUE);
        grupoMembroRepository.save(dono);

        return toGrupoResponse(salvo);
    }

    @Transactional
    public GrupoMembroResponse adicionarMembro(Long grupoId, GrupoMembroRequest request) {
        Grupo grupo = buscarGrupo(grupoId);
        Usuario usuario = usuarioService.buscarEntidade(request.usuarioId());

        GrupoMembro membro = grupoMembroRepository.findByGrupoIdAndUsuarioId(grupoId, request.usuarioId()).orElseGet(GrupoMembro::new);
        membro.setGrupo(grupo);
        membro.setUsuario(usuario);
        membro.setRole(request.role());
        membro.setAtivo(Boolean.TRUE);

        return toGrupoMembroResponse(grupoMembroRepository.save(membro));
    }

    public List<GrupoResponse> listarGrupos() {
        return grupoRepository.findAllByAtivoTrueOrderByNomeAsc().stream().map(this::toGrupoResponse).toList();
    }

    @Transactional
    public PostagemResponse criarPostagem(PostagemRequest request) {
        Postagem postagem = new Postagem();
        postagem.setAutor(usuarioService.buscarEntidade(request.autorId()));
        postagem.setTipo(request.tipo());
        postagem.setVisibilidade(request.visibilidade());
        postagem.setConteudo(request.conteudo());
        postagem.setMidiaUrl(request.midiaUrl());
        postagem.setAtivo(Boolean.TRUE);

        if (request.grupoId() != null) {
            postagem.setGrupo(buscarGrupo(request.grupoId()));
        }
        if (request.registroTreinoId() != null) {
            postagem.setRegistroTreino(registroTreinoService.buscarEntidadeInterna(request.registroTreinoId()));
        }
        if (request.evolucaoFisicaId() != null) {
            postagem.setEvolucaoFisica(evolucaoFisicaService.buscarEntidadeInterna(request.evolucaoFisicaId()));
        }

        return toPostagemResponse(postagemRepository.save(postagem));
    }

    @Transactional
    public void curtirPostagem(Long postagemId, Long usuarioId) {
        Postagem postagem = buscarPostagem(postagemId);
        Usuario usuario = usuarioService.buscarEntidade(usuarioId);

        if (postagemCurtidaRepository.findByPostagemIdAndUsuarioId(postagemId, usuarioId).isPresent()) {
            return;
        }

        PostagemCurtida curtida = new PostagemCurtida();
        curtida.setPostagem(postagem);
        curtida.setUsuario(usuario);
        postagemCurtidaRepository.save(curtida);
    }

    @Transactional
    public PostagemComentarioResponse comentar(Long postagemId, PostagemComentarioRequest request) {
        PostagemComentario comentario = new PostagemComentario();
        comentario.setPostagem(buscarPostagem(postagemId));
        comentario.setAutor(usuarioService.buscarEntidade(request.autorId()));
        comentario.setConteudo(request.conteudo());
        comentario.setAtivo(Boolean.TRUE);
        return toComentarioResponse(postagemComentarioRepository.save(comentario));
    }

    public List<PostagemResponse> listarPostagensUsuario(Long usuarioId) {
        return postagemRepository.findAllByAutorIdAndAtivoTrueOrderByCriadoEmDesc(usuarioId).stream().map(this::toPostagemResponse).toList();
    }

    public List<PostagemResponse> listarFeed(Long usuarioId) {
        Set<Long> autores = new LinkedHashSet<>();
        autores.add(usuarioId);
        seguidorRepository.findAllBySeguidorIdAndAtivoTrue(usuarioId).forEach(item -> autores.add(item.getSeguido().getId()));

        List<Postagem> postagens = new ArrayList<>(postagemRepository.findAllByAutorIdInAndAtivoTrueOrderByCriadoEmDesc(new ArrayList<>(autores)));
        grupoMembroRepository.findAllByUsuarioIdAndAtivoTrue(usuarioId)
                .forEach(membro -> postagens.addAll(postagemRepository.findAllByGrupoIdAndAtivoTrueOrderByCriadoEmDesc(membro.getGrupo().getId())));

        return postagens.stream()
                .distinct()
                .sorted((a, b) -> b.getCriadoEm().compareTo(a.getCriadoEm()))
                .map(this::toPostagemResponse)
                .toList();
    }

    private Seguidor toSeguidor(Long seguidorId, Long seguidoId) {
        return seguidorRepository.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Relacionamento de seguidores nao encontrado."));
    }

    private Grupo buscarGrupo(Long id) {
        return grupoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Grupo nao encontrado."));
    }

    private Postagem buscarPostagem(Long id) {
        return postagemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Postagem nao encontrada."));
    }

    private SeguidorResponse toResponse(Seguidor seguidor) {
        return new SeguidorResponse(
                seguidor.getId(),
                seguidor.getSeguidor().getId(),
                seguidor.getSeguidor().getUsername(),
                seguidor.getSeguido().getId(),
                seguidor.getSeguido().getUsername(),
                seguidor.getAtivo()
        );
    }

    private GrupoResponse toGrupoResponse(Grupo grupo) {
        List<GrupoMembroResponse> membros = grupoMembroRepository.findAllByGrupoIdAndAtivoTrue(grupo.getId())
                .stream()
                .map(this::toGrupoMembroResponse)
                .toList();

        return new GrupoResponse(
                grupo.getId(),
                grupo.getNome(),
                grupo.getDescricao(),
                grupo.getCriador().getId(),
                grupo.getCriador().getUsername(),
                grupo.getVisibilidade(),
                grupo.getAtivo(),
                membros
        );
    }

    private GrupoMembroResponse toGrupoMembroResponse(GrupoMembro membro) {
        return new GrupoMembroResponse(
                membro.getId(),
                membro.getUsuario().getId(),
                membro.getUsuario().getUsername(),
                membro.getRole(),
                membro.getAtivo()
        );
    }

    private PostagemResponse toPostagemResponse(Postagem postagem) {
        List<PostagemComentarioResponse> comentarios = postagemComentarioRepository.findAllByPostagemIdAndAtivoTrueOrderByCriadoEmAsc(postagem.getId())
                .stream()
                .map(this::toComentarioResponse)
                .toList();

        return new PostagemResponse(
                postagem.getId(),
                postagem.getAutor().getId(),
                postagem.getAutor().getUsername(),
                postagem.getTipo(),
                postagem.getVisibilidade(),
                postagem.getGrupo() != null ? postagem.getGrupo().getId() : null,
                postagem.getRegistroTreino() != null ? postagem.getRegistroTreino().getId() : null,
                postagem.getEvolucaoFisica() != null ? postagem.getEvolucaoFisica().getId() : null,
                postagem.getConteudo(),
                postagem.getMidiaUrl(),
                postagemCurtidaRepository.findAllByPostagemId(postagem.getId()).size(),
                comentarios,
                postagem.getCriadoEm()
        );
    }

    private PostagemComentarioResponse toComentarioResponse(PostagemComentario comentario) {
        return new PostagemComentarioResponse(
                comentario.getId(),
                comentario.getAutor().getId(),
                comentario.getAutor().getUsername(),
                comentario.getConteudo(),
                comentario.getCriadoEm()
        );
    }
}
