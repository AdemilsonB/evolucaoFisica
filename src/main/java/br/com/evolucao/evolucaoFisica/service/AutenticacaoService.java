package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.UsuarioIdentidadeExternaRequest;
import br.com.evolucao.evolucaoFisica.dto.UsuarioIdentidadeExternaResponse;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.entity.UsuarioIdentidadeExterna;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.UsuarioIdentidadeExternaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public AutenticacaoService(
            UsuarioIdentidadeExternaRepository usuarioIdentidadeExternaRepository,
            UsuarioService usuarioService
    ) {
        this.usuarioIdentidadeExternaRepository = usuarioIdentidadeExternaRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public UsuarioIdentidadeExternaResponse vincularIdentidadeExterna(UsuarioIdentidadeExternaRequest request) {
        validar(request);

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
        identidade.setEmailExterno(request.emailExterno());
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

    public UsuarioIdentidadeExternaResponse buscarPorProvedorEIdentificador(String identificadorExterno, br.com.evolucao.evolucaoFisica.enumeration.ProvedorAutenticacao provedor) {
        return usuarioIdentidadeExternaRepository.findByProvedorAndIdentificadorExterno(provedor, identificadorExterno)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Identidade externa nao encontrada."));
    }

    private void validar(UsuarioIdentidadeExternaRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados da identidade externa precisam ser informados.");
        }
        if (request.identificadorExterno() == null || request.identificadorExterno().isBlank()) {
            throw new BusinessException("O identificador externo precisa ser informado.");
        }
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
