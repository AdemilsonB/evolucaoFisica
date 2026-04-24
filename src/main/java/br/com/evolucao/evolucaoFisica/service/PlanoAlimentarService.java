package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarDiaRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarDiaResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoAlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoAlimentoResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarResponse;
import br.com.evolucao.evolucaoFisica.entity.Alimento;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentar;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarDia;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarRefeicao;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarRefeicaoAlimento;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.AlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarDiaRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarRefeicaoAlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarRefeicaoRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlanoAlimentarService {

    private final PlanoAlimentarRepository planoAlimentarRepository;
    private final PlanoAlimentarDiaRepository planoAlimentarDiaRepository;
    private final PlanoAlimentarRefeicaoRepository planoAlimentarRefeicaoRepository;
    private final PlanoAlimentarRefeicaoAlimentoRepository planoAlimentarRefeicaoAlimentoRepository;
    private final AlimentoRepository alimentoRepository;
    private final UsuarioService usuarioService;

    public PlanoAlimentarService(
            PlanoAlimentarRepository planoAlimentarRepository,
            PlanoAlimentarDiaRepository planoAlimentarDiaRepository,
            PlanoAlimentarRefeicaoRepository planoAlimentarRefeicaoRepository,
            PlanoAlimentarRefeicaoAlimentoRepository planoAlimentarRefeicaoAlimentoRepository,
            AlimentoRepository alimentoRepository,
            UsuarioService usuarioService
    ) {
        this.planoAlimentarRepository = planoAlimentarRepository;
        this.planoAlimentarDiaRepository = planoAlimentarDiaRepository;
        this.planoAlimentarRefeicaoRepository = planoAlimentarRefeicaoRepository;
        this.planoAlimentarRefeicaoAlimentoRepository = planoAlimentarRefeicaoAlimentoRepository;
        this.alimentoRepository = alimentoRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public PlanoAlimentarResponse criarPlano(PlanoAlimentarRequest request) {
        PlanoAlimentar plano = new PlanoAlimentar();
        plano.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        plano.setNome(request.nome());
        plano.setDescricao(request.descricao());
        plano.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
        plano.setPublico(request.publico() == null ? Boolean.FALSE : request.publico());
        return toResponse(planoAlimentarRepository.save(plano));
    }

    public List<PlanoAlimentarResponse> listarPorUsuario(Long usuarioId) {
        return planoAlimentarRepository.findAllByUsuarioIdOrderByNomeAsc(usuarioId).stream().map(this::toResponse).toList();
    }

    public List<PlanoAlimentarDiaResponse> listarDiaDaSemana(Long usuarioId, DayOfWeek diaSemana) {
        return planoAlimentarDiaRepository.findAllByPlanoAlimentarUsuarioIdAndDiaSemanaOrderByTituloAsc(usuarioId, diaSemana)
                .stream()
                .map(this::toDiaResponse)
                .toList();
    }

    @Transactional
    public PlanoAlimentarDiaResponse adicionarDia(Long planoId, PlanoAlimentarDiaRequest request) {
        PlanoAlimentar plano = buscarPlano(planoId);
        PlanoAlimentarDia dia = new PlanoAlimentarDia();
        dia.setPlanoAlimentar(plano);
        dia.setDiaSemana(request.diaSemana());
        dia.setTitulo(request.titulo());
        return toDiaResponse(planoAlimentarDiaRepository.save(dia));
    }

    @Transactional
    public PlanoAlimentarRefeicaoResponse adicionarRefeicao(Long planoDiaId, PlanoAlimentarRefeicaoRequest request) {
        PlanoAlimentarDia planoDia = buscarDia(planoDiaId);
        PlanoAlimentarRefeicao refeicao = new PlanoAlimentarRefeicao();
        refeicao.setPlanoAlimentarDia(planoDia);
        refeicao.setTipoRefeicao(request.tipoRefeicao());
        refeicao.setHorarioSugerido(request.horarioSugerido());
        refeicao.setObservacao(request.observacao());
        return toRefeicaoResponse(planoAlimentarRefeicaoRepository.save(refeicao));
    }

    @Transactional
    public PlanoAlimentarRefeicaoAlimentoResponse adicionarAlimento(Long planoRefeicaoId, PlanoAlimentarRefeicaoAlimentoRequest request) {
        PlanoAlimentarRefeicao planoRefeicao = buscarRefeicao(planoRefeicaoId);
        Alimento alimento = alimentoRepository.findById(request.alimentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alimento nao encontrado."));

        PlanoAlimentarRefeicaoAlimento item = new PlanoAlimentarRefeicaoAlimento();
        item.setPlanoAlimentarRefeicao(planoRefeicao);
        item.setAlimento(alimento);
        item.setQuantidade(request.quantidade());
        return toAlimentoResponse(planoAlimentarRefeicaoAlimentoRepository.save(item));
    }

    private PlanoAlimentar buscarPlano(Long id) {
        return planoAlimentarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano alimentar nao encontrado."));
    }

    private PlanoAlimentarDia buscarDia(Long id) {
        return planoAlimentarDiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dia do plano alimentar nao encontrado."));
    }

    private PlanoAlimentarRefeicao buscarRefeicao(Long id) {
        return planoAlimentarRefeicaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refeicao planejada nao encontrada."));
    }

    private PlanoAlimentarResponse toResponse(PlanoAlimentar plano) {
        List<PlanoAlimentarDiaResponse> dias = planoAlimentarDiaRepository.findAllByPlanoAlimentarIdOrderByDiaSemanaAsc(plano.getId())
                .stream()
                .map(this::toDiaResponse)
                .toList();

        return new PlanoAlimentarResponse(
                plano.getId(),
                plano.getUsuario().getId(),
                plano.getNome(),
                plano.getDescricao(),
                plano.getAtivo(),
                plano.getPublico(),
                dias
        );
    }

    private PlanoAlimentarDiaResponse toDiaResponse(PlanoAlimentarDia dia) {
        List<PlanoAlimentarRefeicaoResponse> refeicoes = planoAlimentarRefeicaoRepository.findAllByPlanoAlimentarDiaIdOrderByHorarioSugeridoAscIdAsc(dia.getId())
                .stream()
                .map(this::toRefeicaoResponse)
                .toList();

        return new PlanoAlimentarDiaResponse(dia.getId(), dia.getDiaSemana(), dia.getTitulo(), refeicoes);
    }

    private PlanoAlimentarRefeicaoResponse toRefeicaoResponse(PlanoAlimentarRefeicao refeicao) {
        List<PlanoAlimentarRefeicaoAlimentoResponse> alimentos = planoAlimentarRefeicaoAlimentoRepository
                .findAllByPlanoAlimentarRefeicaoIdOrderByIdAsc(refeicao.getId())
                .stream()
                .map(this::toAlimentoResponse)
                .toList();

        return new PlanoAlimentarRefeicaoResponse(
                refeicao.getId(),
                refeicao.getTipoRefeicao(),
                refeicao.getHorarioSugerido(),
                refeicao.getObservacao(),
                alimentos
        );
    }

    private PlanoAlimentarRefeicaoAlimentoResponse toAlimentoResponse(PlanoAlimentarRefeicaoAlimento item) {
        return new PlanoAlimentarRefeicaoAlimentoResponse(
                item.getId(),
                item.getAlimento().getId(),
                item.getAlimento().getNome(),
                item.getQuantidade()
        );
    }
}
