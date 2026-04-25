package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.MetaAtletaRequest;
import br.com.evolucao.evolucaoFisica.dto.MetaAtletaResponse;
import br.com.evolucao.evolucaoFisica.dto.OnboardingRequest;
import br.com.evolucao.evolucaoFisica.dto.OnboardingResponse;
import br.com.evolucao.evolucaoFisica.dto.UsuarioResponse;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class OnboardingService {

    private static final Logger log = LoggerFactory.getLogger(OnboardingService.class);

    private final UsuarioService usuarioService;
    private final MetaAtletaService metaAtletaService;

    public OnboardingService(UsuarioService usuarioService, MetaAtletaService metaAtletaService) {
        this.usuarioService = usuarioService;
        this.metaAtletaService = metaAtletaService;
    }

    @Transactional
    public OnboardingResponse concluir(Long usuarioId, OnboardingRequest request) {
        validar(request);
        Usuario usuario = usuarioService.buscarEntidade(usuarioId);
        usuario.setObjetivo(request.objetivo());
        usuario.setPesoAtual(request.pesoAtual());
        usuario.setAltura(request.altura());
        usuario.setNivelExperiencia(request.nivelExperiencia());
        usuario.setOnboardingConcluido(Boolean.TRUE);
        Usuario salvo = usuarioService.salvar(usuario);

        MetaAtletaRequest metaAtletaRequest = new MetaAtletaRequest(
                request.frequenciaSemanalMeta(),
                request.proteinaDiariaMeta(),
                request.caloriaDiariaMeta(),
                request.observacaoMeta()
        );
        MetaAtletaResponse metaAtleta = metaAtletaService.salvar(usuarioId, metaAtletaRequest);
        UsuarioResponse usuarioResponse = usuarioService.buscarPorId(salvo.getId());
        log.info("Onboarding concluido para usuarioId={}", usuarioId);
        return new OnboardingResponse(usuarioResponse, metaAtleta);
    }

    private void validar(OnboardingRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados de onboarding precisam ser informados.");
        }
        validarPositivo(request.pesoAtual(), "O peso atual deve ser maior que zero.");
        validarPositivo(request.altura(), "A altura deve ser maior que zero.");
    }

    private void validarPositivo(BigDecimal valor, String mensagem) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(mensagem);
        }
    }
}
