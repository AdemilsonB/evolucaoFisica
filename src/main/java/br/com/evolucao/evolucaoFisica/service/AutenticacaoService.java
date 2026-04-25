package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.AuthResponse;
import br.com.evolucao.evolucaoFisica.dto.CadastroLocalRequest;
import br.com.evolucao.evolucaoFisica.dto.LoginGoogleRequest;
import br.com.evolucao.evolucaoFisica.dto.LoginLocalRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioIdentidadeExternaRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioIdentidadeExternaResponse;
import br.com.evolucao.evolucaoFisica.dto.UsuarioResponse;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.entity.UsuarioIdentidadeExterna;
import br.com.evolucao.evolucaoFisica.enumeration.ProvedorAutenticacao;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.UsuarioIdentidadeExternaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AutenticacaoService {

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoService.class);

    private final UsuarioIdentidadeExternaRepository usuarioIdentidadeExternaRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final GoogleTokenService googleTokenService;

    public AutenticacaoService(
            UsuarioIdentidadeExternaRepository usuarioIdentidadeExternaRepository,
            UsuarioService usuarioService,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            GoogleTokenService googleTokenService
    ) {
        this.usuarioIdentidadeExternaRepository = usuarioIdentidadeExternaRepository;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.googleTokenService = googleTokenService;
    }

    @Transactional
    public AuthResponse cadastrarLocal(CadastroLocalRequest request) {
        UsuarioResponse usuario = usuarioService.cadastrarLocal(request);
        Usuario salvo = usuarioService.buscarEntidade(usuario.id());
        usuarioService.atualizarUltimoLogin(salvo);
        log.info("Cadastro local concluido usuarioId={}", usuario.id());
        return criarAuthResponse(salvo);
    }

    @Transactional
    public AuthResponse loginLocal(LoginLocalRequest request) {
        validarLoginLocal(request);
        Usuario usuario = usuarioService.buscarPorEmail(request.email());
        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new BusinessException("O usuario informado esta inativo.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new BusinessException("Esta conta nao possui senha local configurada.");
        }
        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BusinessException("Credenciais invalidas.");
        }

        usuarioService.atualizarUltimoLogin(usuario);
        log.info("Login local realizado usuarioId={}", usuario.getId());
        return criarAuthResponse(usuario);
    }

    @Transactional
    public AuthResponse loginGoogle(LoginGoogleRequest request) {
        if (request == null || request.idToken() == null || request.idToken().isBlank()) {
            throw new BusinessException("O id token do Google precisa ser informado.");
        }

        Jwt jwt = googleTokenService.validarIdToken(request.idToken());
        String identificadorExterno = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String nome = jwt.getClaimAsString("name");
        String fotoPerfilUrl = jwt.getClaimAsString("picture");

        UsuarioIdentidadeExterna identidade = usuarioIdentidadeExternaRepository
                .findByProvedorAndIdentificadorExterno(ProvedorAutenticacao.GOOGLE, identificadorExterno)
                .orElse(null);

        Usuario usuario;
        if (identidade != null) {
            usuario = identidade.getUsuario();
            identidade.setEmailExterno(email);
            identidade.setNomeExibicao(nome);
            identidade.setFotoPerfilUrl(fotoPerfilUrl);
            identidade.setUltimoLoginEm(LocalDateTime.now());
            usuarioIdentidadeExternaRepository.save(identidade);
        } else {
            usuario = usuarioService.buscarPorEmailOpcional(email)
                    .orElseGet(() -> criarUsuarioGoogle(request, email, nome, fotoPerfilUrl));
            vincularIdentidadeGoogle(usuario, identificadorExterno, email, nome, fotoPerfilUrl);
        }

        usuarioService.atualizarUltimoLogin(usuario);
        log.info("Login Google realizado usuarioId={}", usuario.getId());
        return criarAuthResponse(usuario);
    }

    @Transactional
    public UsuarioIdentidadeExternaResponse vincularIdentidadeExterna(UsuarioIdentidadeExternaRequest request) {
        validarVinculoIdentidade(request);

        if (usuarioIdentidadeExternaRepository.existsByProvedorAndIdentificadorExterno(
                request.provedor(),
                request.identificadorExterno().trim()
        )) {
            throw new BusinessException("Ja existe conta vinculada a esta identidade externa.");
        }

        Usuario usuario = usuarioService.buscarEntidade(request.usuarioId());
        UsuarioIdentidadeExterna identidade = new UsuarioIdentidadeExterna();
        identidade.setUsuario(usuario);
        identidade.setProvedor(request.provedor());
        identidade.setIdentificadorExterno(request.identificadorExterno().trim());
        identidade.setEmailExterno(normalizarEmail(request.emailExterno()));
        identidade.setNomeExibicao(request.nomeExibicao());
        identidade.setFotoPerfilUrl(request.fotoPerfilUrl());
        identidade.setUltimoLoginEm(LocalDateTime.now());

        UsuarioIdentidadeExterna salvo = usuarioIdentidadeExternaRepository.save(identidade);
        log.info("Identidade externa vinculada para usuarioId={} provedor={}", usuario.getId(), request.provedor());
        return toResponse(salvo);
    }

    public List<UsuarioIdentidadeExternaResponse> listarIdentidadesPorUsuario(Long usuarioId) {
        usuarioService.buscarEntidade(usuarioId);
        return usuarioIdentidadeExternaRepository.findAllByUsuarioIdOrderByCriadoEmAsc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioIdentidadeExternaResponse buscarPorProvedorEIdentificador(String identificadorExterno, ProvedorAutenticacao provedor) {
        return usuarioIdentidadeExternaRepository.findByProvedorAndIdentificadorExterno(provedor, identificadorExterno)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Identidade externa nao encontrada."));
    }

    private void validarLoginLocal(LoginLocalRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados de login precisam ser informados.");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("O email precisa ser informado.");
        }
        if (request.senha() == null || request.senha().isBlank()) {
            throw new BusinessException("A senha precisa ser informada.");
        }
    }

    private void validarVinculoIdentidade(UsuarioIdentidadeExternaRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados da identidade externa precisam ser informados.");
        }
        if (request.identificadorExterno() == null || request.identificadorExterno().isBlank()) {
            throw new BusinessException("O identificador externo precisa ser informado.");
        }
    }

    private Usuario criarUsuarioGoogle(LoginGoogleRequest request, String email, String nome, String fotoPerfilUrl) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("O Google nao retornou um email valido para esta conta.");
        }
        if (request.username() == null || request.username().isBlank()) {
            throw new BusinessException("Para o primeiro login Google sem conta existente, informe um username.");
        }

        return usuarioService.criarUsuarioSocial(
                nome != null && !nome.isBlank() ? nome : request.username().trim(),
                email,
                request.username().trim(),
                fotoPerfilUrl
        );
    }

    private void vincularIdentidadeGoogle(
            Usuario usuario,
            String identificadorExterno,
            String emailExterno,
            String nomeExibicao,
            String fotoPerfilUrl
    ) {
        UsuarioIdentidadeExterna identidade = new UsuarioIdentidadeExterna();
        identidade.setUsuario(usuario);
        identidade.setProvedor(ProvedorAutenticacao.GOOGLE);
        identidade.setIdentificadorExterno(identificadorExterno);
        identidade.setEmailExterno(normalizarEmail(emailExterno));
        identidade.setNomeExibicao(nomeExibicao);
        identidade.setFotoPerfilUrl(fotoPerfilUrl);
        identidade.setUltimoLoginEm(LocalDateTime.now());
        usuarioIdentidadeExternaRepository.save(identidade);
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private AuthResponse criarAuthResponse(Usuario usuario) {
        Usuario salvo = usuarioService.buscarEntidade(usuario.getId());
        String token = jwtTokenService.gerarToken(salvo);
        return new AuthResponse(
                token,
                "Bearer",
                jwtTokenService.calcularExpiracao(),
                !Boolean.TRUE.equals(salvo.getOnboardingConcluido()),
                usuarioService.buscarPorId(salvo.getId())
        );
    }

    private UsuarioIdentidadeExternaResponse toResponse(UsuarioIdentidadeExterna identidade) {
        return new UsuarioIdentidadeExternaResponse(
                identidade.getId(),
                identidade.getUsuario().getId(),
                identidade.getProvedor(),
                identidade.getIdentificadorExterno(),
                identidade.getEmailExterno(),
                identidade.getNomeExibicao(),
                identidade.getFotoPerfilUrl(),
                identidade.getUltimoLoginEm(),
                identidade.getCriadoEm()
        );
    }
}
