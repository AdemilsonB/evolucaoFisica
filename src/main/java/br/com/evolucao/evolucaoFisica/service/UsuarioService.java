package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.AtualizarPesoRequest;
import br.com.evolucao.evolucaoFisica.dto.CadastroLocalRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioResponse;
import br.com.evolucao.evolucaoFisica.entity.PerfilGamificacaoUsuario;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.PerfilGamificacaoUsuarioRepository;
import br.com.evolucao.evolucaoFisica.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PerfilGamificacaoUsuarioRepository perfilGamificacaoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PerfilGamificacaoUsuarioRepository perfilGamificacaoUsuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.perfilGamificacaoUsuarioRepository = perfilGamificacaoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse criarUsuario(UsuarioRequest request) {
        validarDuplicidade(request.email(), request.username(), null);
        validarSenhaObrigatoria(request.senha());

        Usuario usuario = new Usuario();
        preencherUsuario(usuario, request);
        usuario.setDataCriacao(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);
        criarPerfilGamificadoInicial(salvo);
        log.info("Usuario criado via fluxo interno usuarioId={}", salvo.getId());
        return toResponse(salvo);
    }

    @Transactional
    public UsuarioResponse cadastrarLocal(CadastroLocalRequest request) {
        validarCadastroLocal(request);
        validarDuplicidade(request.email(), request.username(), null);

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(normalizarEmail(request.email()));
        usuario.setUsername(request.username().trim());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setAtivo(Boolean.TRUE);
        usuario.setPerfilPrivado(Boolean.FALSE);
        usuario.setOnboardingConcluido(Boolean.FALSE);
        usuario.setDataCriacao(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);
        criarPerfilGamificadoInicial(salvo);
        log.info("Usuario criado via cadastro local usuarioId={}", salvo.getId());
        return toResponse(salvo);
    }

    @Transactional
    public Usuario criarUsuarioSocial(String nome, String email, String username, String fotoPerfilUrl) {
        if (email == null || email.isBlank()) {
            throw new BusinessException("O email do usuario social precisa ser informado.");
        }
        if (username == null || username.isBlank()) {
            throw new BusinessException("O username do usuario social precisa ser informado.");
        }
        validarDuplicidade(email, username, null);

        Usuario usuario = new Usuario();
        usuario.setNome(nome == null || nome.isBlank() ? username.trim() : nome.trim());
        usuario.setEmail(normalizarEmail(email));
        usuario.setUsername(username.trim());
        usuario.setSenha(null);
        usuario.setFotoPerfilUrl(fotoPerfilUrl);
        usuario.setAtivo(Boolean.TRUE);
        usuario.setPerfilPrivado(Boolean.FALSE);
        usuario.setOnboardingConcluido(Boolean.FALSE);
        usuario.setDataCriacao(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);
        criarPerfilGamificadoInicial(salvo);
        log.info("Usuario criado via login social usuarioId={}", salvo.getId());
        return salvo;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarEntidade(id);

        validarDuplicidade(request.email(), request.username(), usuario);

        preencherUsuario(usuario, request);
        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse atualizarPeso(Long id, AtualizarPesoRequest request) {
        Usuario usuario = buscarEntidade(id);
        usuario.setPesoAtual(request.pesoAtual());
        return toResponse(usuarioRepository.save(usuario));
    }

    public Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado."));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(normalizarEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado para o email informado."));
    }

    public Optional<Usuario> buscarPorEmailOpcional(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return usuarioRepository.findByEmailIgnoreCase(normalizarEmail(email));
    }

    public Optional<Usuario> buscarPorUsernameOpcional(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }
        return usuarioRepository.findByUsernameIgnoreCase(username.trim());
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizarUltimoLogin(Usuario usuario) {
        usuario.setUltimoLoginEm(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    private void preencherUsuario(Usuario usuario, UsuarioRequest request) {
        usuario.setNome(request.nome().trim());
        usuario.setEmail(normalizarEmail(request.email()));
        usuario.setUsername(request.username().trim());
        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }
        usuario.setTelefone(request.telefone());
        usuario.setBio(request.bio());
        usuario.setFotoPerfilUrl(request.fotoPerfilUrl());
        usuario.setPesoAtual(request.pesoAtual());
        usuario.setAltura(request.altura());
        usuario.setObjetivo(request.objetivo());
        usuario.setDataNascimento(request.dataNascimento());
        usuario.setCidade(request.cidade());
        usuario.setEstado(request.estado());
        usuario.setPerfilPrivado(request.perfilPrivado() == null ? Boolean.FALSE : request.perfilPrivado());
        usuario.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
    }

    private void criarPerfilGamificadoInicial(Usuario usuario) {
        PerfilGamificacaoUsuario perfil = new PerfilGamificacaoUsuario();
        perfil.setUsuario(usuario);
        perfil.setDataInicio(usuario.getDataCriacao().toLocalDate());
        perfil.setPesoInicial(usuario.getPesoAtual());
        perfil.setPesoAtual(usuario.getPesoAtual());
        perfilGamificacaoUsuarioRepository.save(perfil);
    }

    private void validarCadastroLocal(CadastroLocalRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados de cadastro precisam ser informados.");
        }
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("O nome do usuario e obrigatorio.");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BusinessException("O email do usuario e obrigatorio.");
        }
        if (request.username() == null || request.username().isBlank()) {
            throw new BusinessException("O username do usuario e obrigatorio.");
        }
        validarSenhaObrigatoria(request.senha());
    }

    private void validarSenhaObrigatoria(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new BusinessException("A senha do usuario e obrigatoria.");
        }
    }

    private void validarDuplicidade(String email, String username, Usuario usuarioExistente) {
        String emailNormalizado = normalizarEmail(email);
        if (emailNormalizado != null) {
            Optional<Usuario> usuarioPorEmail = usuarioRepository.findByEmailIgnoreCase(emailNormalizado);
            if (usuarioPorEmail.isPresent() && (usuarioExistente == null || !usuarioPorEmail.get().getId().equals(usuarioExistente.getId()))) {
                throw new BusinessException("Ja existe usuario cadastrado com este email.");
            }
        }

        String usernameNormalizado = username != null ? username.trim() : null;
        if (usernameNormalizado != null && !usernameNormalizado.isBlank()) {
            Optional<Usuario> usuarioPorUsername = usuarioRepository.findByUsernameIgnoreCase(usernameNormalizado);
            if (usuarioPorUsername.isPresent() && (usuarioExistente == null || !usuarioPorUsername.get().getId().equals(usuarioExistente.getId()))) {
                throw new BusinessException("Ja existe usuario cadastrado com este username.");
            }
        }
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getTelefone(),
                usuario.getBio(),
                usuario.getFotoPerfilUrl(),
                usuario.getPesoAtual(),
                usuario.getAltura(),
                usuario.getObjetivo(),
                usuario.getNivelExperiencia(),
                usuario.getDataNascimento(),
                usuario.getCidade(),
                usuario.getEstado(),
                usuario.getPerfilPrivado(),
                usuario.getOnboardingConcluido(),
                usuario.getRoleSistema(),
                usuario.getAtivo(),
                usuario.getUltimoLoginEm(),
                usuario.getDataCriacao()
        );
    }
}
