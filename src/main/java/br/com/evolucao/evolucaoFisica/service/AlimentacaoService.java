package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.RefeicaoAlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoAlimentoResponse;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoRequest;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoResponse;
import br.com.evolucao.evolucaoFisica.entity.Alimento;
import br.com.evolucao.evolucaoFisica.entity.Refeicao;
import br.com.evolucao.evolucaoFisica.entity.RefeicaoAlimento;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.AlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.RefeicaoAlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.RefeicaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlimentacaoService {

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
        Refeicao refeicao = new Refeicao();
        refeicao.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        refeicao.setData(request.data());
        refeicao.setTipo(request.tipo());
        return toResponse(refeicaoRepository.save(refeicao));
    }

    @Transactional
    public RefeicaoAlimentoResponse adicionarAlimento(Long refeicaoId, RefeicaoAlimentoRequest request) {
        Refeicao refeicao = buscarEntidade(refeicaoId);
        Alimento alimento = alimentoRepository.findById(request.alimentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alimento nao encontrado."));

        RefeicaoAlimento refeicaoAlimento = new RefeicaoAlimento();
        refeicaoAlimento.setRefeicao(refeicao);
        refeicaoAlimento.setAlimento(alimento);
        refeicaoAlimento.setQuantidade(request.quantidade());

        return toResponse(refeicaoAlimentoRepository.save(refeicaoAlimento));
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

    private RefeicaoResponse toResponse(Refeicao refeicao) {
        List<RefeicaoAlimentoResponse> alimentos = refeicaoAlimentoRepository.findAllByRefeicaoIdOrderByIdAsc(refeicao.getId())
                .stream()
                .map(this::toResponse)
                .toList();

        return new RefeicaoResponse(
                refeicao.getId(),
                refeicao.getUsuario().getId(),
                refeicao.getData(),
                refeicao.getTipo(),
                alimentos
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
