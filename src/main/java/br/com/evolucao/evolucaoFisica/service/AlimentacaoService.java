package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.RefeicaoAlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoAlimentoResponse;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoRequest;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoResponse;
import br.com.evolucao.evolucaoFisica.entity.Alimento;
import br.com.evolucao.evolucaoFisica.entity.Refeicao;
import br.com.evolucao.evolucaoFisica.entity.RefeicaoAlimento;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.AlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.RefeicaoAlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.RefeicaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlimentacaoService {

    private static final Logger log = LoggerFactory.getLogger(AlimentacaoService.class);

    private final RefeicaoRepository refeicaoRepository;
    private final RefeicaoAlimentoRepository refeicaoAlimentoRepository;
    private final AlimentoRepository alimentoRepository;
    private final UsuarioService usuarioService;

    public AlimentacaoService(
            RefeicaoRepository refeicaoRepository,
            RefeicaoAlimentoRepository refeicaoAlimentoRepository,
            AlimentoRepository alimentoRepository,
            UsuarioService usuarioService
    ) {
        this.refeicaoRepository = refeicaoRepository;
        this.refeicaoAlimentoRepository = refeicaoAlimentoRepository;
        this.alimentoRepository = alimentoRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public RefeicaoResponse registrarRefeicao(RefeicaoRequest request) {
        validarRegistroRefeicao(request);
        Refeicao refeicao = new Refeicao();
        refeicao.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        refeicao.setData(request.data());
        refeicao.setTipo(request.tipo());
        Refeicao salva = refeicaoRepository.save(refeicao);
        log.info("Refeicao registrada para usuarioId={} refeicaoId={}", request.usuarioId(), salva.getId());
        return toResponse(salva);
    }

    @Transactional
    public RefeicaoAlimentoResponse adicionarAlimento(Long refeicaoId, RefeicaoAlimentoRequest request) {
        validarAdicaoAlimento(request);
        Refeicao refeicao = buscarEntidade(refeicaoId);
        Alimento alimento = alimentoRepository.findById(request.alimentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alimento nao encontrado."));

        RefeicaoAlimento refeicaoAlimento = new RefeicaoAlimento();
        refeicaoAlimento.setRefeicao(refeicao);
        refeicaoAlimento.setAlimento(alimento);
        refeicaoAlimento.setQuantidade(request.quantidade());

        RefeicaoAlimento salvo = refeicaoAlimentoRepository.save(refeicaoAlimento);
        log.info("Alimento adicionado em refeicaoId={} alimentoId={}", refeicaoId, request.alimentoId());
        return toResponse(salvo);
    }

    public List<RefeicaoResponse> listarRefeicoes(Long usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return refeicaoRepository.findAllByUsuarioIdAndDataBetweenOrderByDataDesc(usuarioId, dataInicio, dataFim)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RefeicaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    private Refeicao buscarEntidade(Long id) {
        return refeicaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refeicao nao encontrada."));
    }

    private void validarRegistroRefeicao(RefeicaoRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados da refeicao precisam ser informados.");
        }
        if (request.data() == null) {
            throw new BusinessException("A data da refeicao precisa ser informada.");
        }
        if (request.tipo() == null) {
            throw new BusinessException("O tipo da refeicao precisa ser informado.");
        }
    }

    private void validarAdicaoAlimento(RefeicaoAlimentoRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do alimento precisam ser informados.");
        }
        if (request.quantidade() == null || request.quantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("A quantidade do alimento deve ser maior que zero.");
        }
    }

    private RefeicaoResponse toResponse(Refeicao refeicao) {
        List<RefeicaoAlimento> refeicaoAlimentos = refeicaoAlimentoRepository.findAllByRefeicaoIdOrderByIdAsc(refeicao.getId());
        List<RefeicaoAlimentoResponse> alimentos = refeicaoAlimentos.stream()
                .map(this::toResponse)
                .toList();

        BigDecimal caloriasTotais = BigDecimal.ZERO;
        BigDecimal proteinaTotal = BigDecimal.ZERO;
        BigDecimal carboidratoTotal = BigDecimal.ZERO;
        BigDecimal gorduraTotal = BigDecimal.ZERO;
        BigDecimal acucaresTotais = BigDecimal.ZERO;

        for (RefeicaoAlimento item : refeicaoAlimentos) {
            BigDecimal fator = item.getQuantidade().divide(new BigDecimal("100"), 4, java.math.RoundingMode.HALF_UP);
            caloriasTotais = caloriasTotais.add(item.getAlimento().getCalorias().multiply(fator));
            proteinaTotal = proteinaTotal.add(item.getAlimento().getProteina().multiply(fator));
            carboidratoTotal = carboidratoTotal.add(item.getAlimento().getCarboidrato().multiply(fator));
            gorduraTotal = gorduraTotal.add(item.getAlimento().getGordura().multiply(fator));
            acucaresTotais = acucaresTotais.add(item.getAlimento().getAcucares().multiply(fator));
        }

        return new RefeicaoResponse(
                refeicao.getId(),
                refeicao.getUsuario().getId(),
                refeicao.getData(),
                refeicao.getTipo(),
                alimentos,
                caloriasTotais,
                proteinaTotal,
                carboidratoTotal,
                gorduraTotal,
                acucaresTotais
        );
    }

    private RefeicaoAlimentoResponse toResponse(RefeicaoAlimento refeicaoAlimento) {
        return new RefeicaoAlimentoResponse(
                refeicaoAlimento.getId(),
                refeicaoAlimento.getAlimento().getId(),
                refeicaoAlimento.getAlimento().getNome(),
                refeicaoAlimento.getQuantidade()
        );
    }
}
