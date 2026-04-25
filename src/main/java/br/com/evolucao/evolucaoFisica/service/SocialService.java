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
import br.com.evolucao.evolucaoFisica.enumeration.VisibilidadeConteudo;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.GrupoMembroRepository;
import br.com.evolucao.evolucaoFisica.repository.GrupoRepository;
import br.com.evolucao.evolucaoFisica.repository.PostagemComentarioRepository;
import br.com.evolucao.evolucaoFisica.repository.PostagemCurtidaRepository;
import br.com.evolucao.evolucaoFisica.repository.PostagemRepository;
import br.com.evolucao.evolucaoFisica.repository.SeguidorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class SocialService {

    private static final Logger log = LoggerFactory.getLogger(SocialService.class);

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

        Seguidor salvo = seguidorRepository.save(seguidor);
        log.info("Relacionamento de seguimento ativado seguidorId={} seguidoId={}", seguidorId, seguidoId);
        return toResponse(salvo);
    }

    public List<SeguidorResponse> listarSeguidores(Long usuarioId) {
        return seguidorRepository.findAllBySeguidoIdAndAtivoTrue(usuarioId).stream().map(this::toResponse).toList();
    }

    public List<SeguidorResponse> listarSeguindo(Long usuarioId) {
        return seguidorRepository.findAllBySeguidorIdAndAtivoTrue(usuarioId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public GrupoResponse criarGrupo(GrupoRequest request) {
        validarCriacaoGrupo(request);
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

        log.info("Grupo criado grupoId={} criadorId={}", salvo.getId(), grupo.getCriador().getId());
        return toGrupoResponse(salvo);
    }

    @Transactional
    public GrupoMembroResponse adicionarMembro(Long grupoId, GrupoMembroRequest request) {
        validarGrupoMembro(request);
        Grupo grupo = buscarGrupo(grupoId);
        Usuario usuario = usuarioService.buscarEntidade(request.usuarioId());

        GrupoMembro membro = grupoMembroRepository.findByGrupoIdAndUsuarioId(grupoId, request.usuarioId()).orElseGet(GrupoMembro::new);
        membro.setGrupo(grupo);
        membro.setUsuario(usuario);
        membro.setRole(request.role());
        membro.setAtivo(Boolean.TRUE);

        GrupoMembro salvo = grupoMembroRepository.save(membro);
        log.info("Membro associado ao grupo grupoId={} usuarioId={}", grupoId, request.usuarioId());
        return toGrupoMembroResponse(salvo);
    }

    public List<GrupoResponse> listarGrupos() {
        return grupoRepository.findAllByAtivoTrueOrderByNomeAsc().stream().map(this::toGrupoResponse).toList();
    }

    @Transactional
    public PostagemResponse criarPostagem(PostagemRequest request) {
        validarCriacaoPostagem(request);
        Usuario autor = usuarioService.buscarEntidade(request.autorId());
        Postagem postagem = new Postagem();
        postagem.setAutor(autor);
        postagem.setTipo(request.tipo());
        postagem.setVisibilidade(request.visibilidade());
        postagem.setConteudo(request.conteudo());
        postagem.setMidiaUrl(request.midiaUrl());
        postagem.setAtivo(Boolean.TRUE);

        if (request.grupoId() != null) {
            Grupo grupo = buscarGrupo(request.grupoId());
            if (!isMembroAtivo(grupo.getId(), autor.getId())) {
                throw new BusinessException("Somente membros ativos podem publicar no grupo informado.");
            }
            postagem.setGrupo(grupo);
        }
        if (request.registroTreinoId() != null) {
            var registroTreino = registroTreinoService.buscarEntidadeInterna(request.registroTreinoId());
            if (!registroTreino.getUsuario().getId().equals(autor.getId())) {
                throw new BusinessException("O registro de treino informado nao pertence ao autor da postagem.");
            }
            postagem.setRegistroTreino(registroTreino);
        }
        if (request.evolucaoFisicaId() != null) {
            var evolucao = evolucaoFisicaService.buscarEntidadeInterna(request.evolucaoFisicaId());
            if (!evolucao.getUsuario().getId().equals(autor.getId())) {
                throw new BusinessException("A evolucao fisica informada nao pertence ao autor da postagem.");
            }
            postagem.setEvolucaoFisica(evolucao);
        }

        Postagem salva = postagemRepository.save(postagem);
        log.info("Postagem criada postagemId={} autorId={}", salva.getId(), autor.getId());
        return toPostagemResponse(salva);
    }

    @Transactional
    public void curtirPostagem(Long postagemId, Long usuarioId) {
        Postagem postagem = buscarPostagem(postagemId);
        Usuario usuario = usuarioService.buscarEntidade(usuarioId);
        validarAcessoPostagem(postagem, usuarioId);

        if (postagemCurtidaRepository.findByPostagemIdAndUsuarioId(postagemId, usuarioId).isPresent()) {
            return;
        }

        PostagemCurtida curtida = new PostagemCurtida();
        curtida.setPostagem(postagem);
        curtida.setUsuario(usuario);
        postagemCurtidaRepository.save(curtida);
        log.info("Postagem curtida postagemId={} usuarioId={}", postagemId, usuarioId);
    }

    @Transactional
    public PostagemComentarioResponse comentar(Long postagemId, PostagemComentarioRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do comentario precisam ser informados.");
        }
        if (request.conteudo() == null || request.conteudo().isBlank()) {
            throw new BusinessException("O comentario precisa ter conteudo.");
        }
        Postagem postagem = buscarPostagem(postagemId);
        validarAcessoPostagem(postagem, request.autorId());
        PostagemComentario comentario = new PostagemComentario();
        comentario.setPostagem(postagem);
        comentario.setAutor(usuarioService.buscarEntidade(request.autorId()));
        comentario.setConteudo(request.conteudo());
        comentario.setAtivo(Boolean.TRUE);
        PostagemComentario salvo = postagemComentarioRepository.save(comentario);
        log.info("Comentario criado postagemId={} autorId={}", postagemId, request.autorId());
        return toComentarioResponse(salvo);
    }

    public List<PostagemResponse> listarPostagensUsuario(Long usuarioId) {
        return postagemRepository.findAllByAutorIdAndAtivoTrueOrderByCriadoEmDesc(usuarioId).stream().map(this::toPostagemResponse).toList();
    }

    public List<PostagemResponse> listarFeed(Long usuarioId) {
        usuarioService.buscarEntidade(usuarioId);
        Set<Long> autores = new LinkedHashSet<>();
        autores.add(usuarioId);
        seguidorRepository.findAllBySeguidorIdAndAtivoTrue(usuarioId).forEach(item -> autores.add(item.getSeguido().getId()));

        List<Postagem> postagens = new ArrayList<>(postagemRepository.findAllByAutorIdInAndAtivoTrueOrderByCriadoEmDesc(new ArrayList<>(autores)));
        grupoMembroRepository.findAllByUsuarioIdAndAtivoTrue(usuarioId)
                .forEach(membro -> postagens.addAll(postagemRepository.findAllByGrupoIdAndAtivoTrueOrderByCriadoEmDesc(membro.getGrupo().getId())));

        return postagens.stream()
                .distinct()
                .filter(postagem -> podeVisualizarPostagem(postagem, usuarioId))
                .sorted((a, b) -> b.getCriadoEm().compareTo(a.getCriadoEm()))
                .map(this::toPostagemResponse)
                .toList();
    }

    private void validarCriacaoGrupo(GrupoRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do grupo precisam ser informados.");
        }
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("O nome do grupo e obrigatorio.");
        }
        if (request.visibilidade() == null) {
            throw new BusinessException("A visibilidade do grupo precisa ser informada.");
        }
    }

    private void validarGrupoMembro(GrupoMembroRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do membro do grupo precisam ser informados.");
        }
        if (request.usuarioId() == null) {
            throw new BusinessException("O usuario do grupo precisa ser informado.");
        }
        if (request.role() == null) {
            throw new BusinessException("O papel do membro precisa ser informado.");
        }
    }

    private void validarCriacaoPostagem(PostagemRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados da postagem precisam ser informados.");
        }
        if ((request.conteudo() == null || request.conteudo().isBlank()) && (request.midiaUrl() == null || request.midiaUrl().isBlank())) {
            throw new BusinessException("A postagem precisa ter conteudo textual ou midia.");
        }
        if (request.visibilidade() == VisibilidadeConteudo.GRUPO && request.grupoId() == null) {
            throw new BusinessException("Postagens com visibilidade de grupo precisam informar o grupo.");
        }
        if (request.grupoId() != null && request.visibilidade() != VisibilidadeConteudo.GRUPO) {
            throw new BusinessException("Ao informar grupo, a visibilidade da postagem deve ser GRUPO.");
        }
    }

    private void validarAcessoPostagem(Postagem postagem, Long usuarioId) {
        if (!podeVisualizarPostagem(postagem, usuarioId)) {
            throw new BusinessException("O usuario nao possui permissao para interagir com esta postagem.");
        }
    }

    private boolean podeVisualizarPostagem(Postagem postagem, Long usuarioId) {
        if (postagem.getAutor().getId().equals(usuarioId)) {
            return true;
        }
        return switch (postagem.getVisibilidade()) {
            case PUBLICA -> true;
            case PRIVADA -> false;
            case SEGUIDORES -> isSeguidorAtivo(usuarioId, postagem.getAutor().getId());
            case GRUPO -> postagem.getGrupo() != null && isMembroAtivo(postagem.getGrupo().getId(), usuarioId);
        };
    }

    private boolean isSeguidorAtivo(Long seguidorId, Long seguidoId) {
        return seguidorRepository.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId)
                .map(Seguidor::getAtivo)
                .orElse(Boolean.FALSE);
    }

    private boolean isMembroAtivo(Long grupoId, Long usuarioId) {
        return grupoMembroRepository.findByGrupoIdAndUsuarioId(grupoId, usuarioId)
                .map(GrupoMembro::getAtivo)
                .orElse(Boolean.FALSE);
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
