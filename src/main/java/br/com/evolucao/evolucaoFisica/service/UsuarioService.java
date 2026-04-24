package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.AtualizarPesoRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioResponse;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponse criarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ja existe usuario cadastrado com este email.");
        }
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new BusinessException("Ja existe usuario cadastrado com este username.");
        }

        Usuario usuario = new Usuario();
        preencherUsuario(usuario, request);
        usuario.setDataCriacao(LocalDateTime.now());

        return toResponse(usuarioRepository.save(usuario));
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

        if (!usuario.getEmail().equals(request.email()) && usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ja existe usuario cadastrado com este email.");
        }
        if (!usuario.getUsername().equals(request.username()) && usuarioRepository.existsByUsername(request.username())) {
            throw new BusinessException("Ja existe usuario cadastrado com este username.");
        }

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

    private void preencherUsuario(Usuario usuario, UsuarioRequest request) {
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setUsername(request.username());
        usuario.setSenha(request.senha());
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
                usuario.getDataNascimento(),
                usuario.getCidade(),
                usuario.getEstado(),
                usuario.getPerfilPrivado(),
                usuario.getAtivo(),
                usuario.getDataCriacao()
        );
    }
}
