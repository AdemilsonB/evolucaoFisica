package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.MetaAtletaRequest;
import br.com.evolucao.evolucaoFisica.dto.MetaAtletaResponse;
import br.com.evolucao.evolucaoFisica.entity.MetaAtleta;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.MetaAtletaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class MetaAtletaService {

    private static final Logger log = LoggerFactory.getLogger(MetaAtletaService.class);

    private final MetaAtletaRepository metaAtletaRepository;
    private final UsuarioService usuarioService;

    public MetaAtletaService(MetaAtletaRepository metaAtletaRepository, UsuarioService usuarioService) {
        this.metaAtletaRepository = metaAtletaRepository;
        this.usuarioService = usuarioService;
    }

    public MetaAtletaResponse buscarPorUsuario(Long usuarioId) {
        return toResponse(metaAtletaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Metas do atleta nao encontradas para o usuario informado.")));
    }

    @Transactional
    public MetaAtletaResponse salvar(Long usuarioId, MetaAtletaRequest request) {
        validar(request);

        Usuario usuario = usuarioService.buscarEntidade(usuarioId);
        MetaAtleta metaAtleta = metaAtletaRepository.findByUsuarioId(usuarioId).orElseGet(MetaAtleta::new);

        metaAtleta.setUsuario(usuario);
        metaAtleta.setFrequenciaSemanalMeta(request.frequenciaSemanalMeta());
        metaAtleta.setProteinaDiariaMeta(request.proteinaDiariaMeta());
        metaAtleta.setCaloriaDiariaMeta(request.caloriaDiariaMeta());
        metaAtleta.setObservacao(request.observacao());

        MetaAtleta salvo = metaAtletaRepository.save(metaAtleta);
        log.info("Metas do atleta salvas para usuarioId={}", usuarioId);
        return toResponse(salvo);
    }

    private void validar(MetaAtletaRequest request) {
        if (request == null) {
            throw new BusinessException("As metas do atleta precisam ser informadas.");
        }
        validarPositivo(request.frequenciaSemanalMeta(), "A meta de frequencia semanal deve ser maior que zero.");
        validarPositivo(request.proteinaDiariaMeta(), "A meta de proteina diaria deve ser maior que zero.");
        validarPositivo(request.caloriaDiariaMeta(), "A meta calorica diaria deve ser maior que zero.");
    }

    private void validarPositivo(Integer valor, String mensagem) {
        if (valor != null && valor <= 0) {
            throw new BusinessException(mensagem);
        }
    }

    private void validarPositivo(BigDecimal valor, String mensagem) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(mensagem);
        }
    }

    private MetaAtletaResponse toResponse(MetaAtleta metaAtleta) {
        return new MetaAtletaResponse(
                metaAtleta.getId(),
                metaAtleta.getUsuario().getId(),
                metaAtleta.getUsuario().getObjetivo(),
                metaAtleta.getFrequenciaSemanalMeta(),
                metaAtleta.getProteinaDiariaMeta(),
                metaAtleta.getCaloriaDiariaMeta(),
                metaAtleta.getObservacao()
        );
    }
}
