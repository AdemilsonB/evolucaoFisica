package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.ExecucaoPlanoAlimentarDiaResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarDiaRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarDiaResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoAlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoAlimentoResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRefeicaoResponse;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarRequest;
import br.com.evolucao.evolucaoFisica.dto.PlanoAlimentarResponse;
import br.com.evolucao.evolucaoFisica.dto.RegistroDiarioResponse;
import br.com.evolucao.evolucaoFisica.dto.RefeicaoResponse;
import br.com.evolucao.evolucaoFisica.entity.Alimento;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentar;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarDia;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarRefeicao;
import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarRefeicaoAlimento;
import br.com.evolucao.evolucaoFisica.entity.RegistroDiario;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.AlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarDiaRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarRefeicaoAlimentoRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarRefeicaoRepository;
import br.com.evolucao.evolucaoFisica.repository.PlanoAlimentarRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroDiarioRepository;
import br.com.evolucao.evolucaoFisica.repository.RefeicaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlanoAlimentarService {

    private static final Logger log = LoggerFactory.getLogger(PlanoAlimentarService.class);

    private final PlanoAlimentarRepository planoAlimentarRepository;
    private final PlanoAlimentarDiaRepository planoAlimentarDiaRepository;
    private final PlanoAlimentarRefeicaoRepository planoAlimentarRefeicaoRepository;
    private final PlanoAlimentarRefeicaoAlimentoRepository planoAlimentarRefeicaoAlimentoRepository;
    private final AlimentoRepository alimentoRepository;
    private final RefeicaoRepository refeicaoRepository;
    private final RegistroDiarioRepository registroDiarioRepository;
    private final AlimentacaoService alimentacaoService;
    private final UsuarioService usuarioService;

    public PlanoAlimentarService(
            PlanoAlimentarRepository planoAlimentarRepository,
            PlanoAlimentarDiaRepository planoAlimentarDiaRepository,
            PlanoAlimentarRefeicaoRepository planoAlimentarRefeicaoRepository,
            PlanoAlimentarRefeicaoAlimentoRepository planoAlimentarRefeicaoAlimentoRepository,
            AlimentoRepository alimentoRepository,
            RefeicaoRepository refeicaoRepository,
            RegistroDiarioRepository registroDiarioRepository,
            AlimentacaoService alimentacaoService,
            UsuarioService usuarioService
    ) {
        this.planoAlimentarRepository = planoAlimentarRepository;
        this.planoAlimentarDiaRepository = planoAlimentarDiaRepository;
        this.planoAlimentarRefeicaoRepository = planoAlimentarRefeicaoRepository;
        this.planoAlimentarRefeicaoAlimentoRepository = planoAlimentarRefeicaoAlimentoRepository;
        this.alimentoRepository = alimentoRepository;
        this.refeicaoRepository = refeicaoRepository;
        this.registroDiarioRepository = registroDiarioRepository;
        this.alimentacaoService = alimentacaoService;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public PlanoAlimentarResponse criarPlano(PlanoAlimentarRequest request) {
        validarPlano(request);
        PlanoAlimentar plano = new PlanoAlimentar();
        plano.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        plano.setNome(request.nome().trim());
        plano.setDescricao(request.descricao());
        plano.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
        plano.setPublico(request.publico() == null ? Boolean.FALSE : request.publico());
        plano.setPrincipal(request.principal() == null ? Boolean.FALSE : request.principal());
        plano.setDataInicio(request.dataInicio());
        plano.setDataFim(request.dataFim());
        PlanoAlimentar salvo = planoAlimentarRepository.save(plano);
        sincronizarPlanoPrincipal(salvo);
        log.info("Plano alimentar criado planoId={} usuarioId={}", salvo.getId(), request.usuarioId());
        return toResponse(salvo);
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

    public ExecucaoPlanoAlimentarDiaResponse consultarExecucaoDia(Long usuarioId, LocalDate dataReferencia) {
        usuarioService.buscarEntidade(usuarioId);
        PlanoAlimentar plano = planoAlimentarRepository.findPlanoPrincipalAtivo(usuarioId, dataReferencia)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum plano alimentar principal ativo encontrado para a data informada."));

        PlanoAlimentarDia planoDia = planoAlimentarDiaRepository.findByPlanoAlimentarIdAndDiaSemana(plano.getId(), dataReferencia.getDayOfWeek())
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum dia planejado foi encontrado para a data informada."));

        List<PlanoAlimentarRefeicaoResponse> refeicoesPlanejadas = planoAlimentarRefeicaoRepository
                .findAllByPlanoAlimentarDiaIdOrderByHorarioSugeridoAscIdAsc(planoDia.getId())
                .stream()
                .map(this::toRefeicaoResponse)
                .toList();

        List<RefeicaoResponse> refeicoesExecutadas = refeicaoRepository.findAllByUsuarioIdAndDataReferencia(usuarioId, dataReferencia)
                .stream()
                .map(refeicao -> alimentacaoService.buscarPorId(refeicao.getId()))
                .toList();

        RegistroDiarioResponse registroDiario = registroDiarioRepository.findByUsuarioIdAndDataReferencia(usuarioId, dataReferencia)
                .map(this::toRegistroDiarioResponse)
                .orElse(null);

        BigDecimal proteinaTotal = refeicoesExecutadas.stream()
                .map(RefeicaoResponse::proteinaTotal)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExecucaoPlanoAlimentarDiaResponse(
                usuarioId,
                dataReferencia,
                toResponse(plano),
                registroDiario,
                refeicoesPlanejadas,
                refeicoesExecutadas,
                proteinaTotal,
                refeicoesPlanejadas.size(),
                refeicoesExecutadas.size()
        );
    }

    @Transactional
    public PlanoAlimentarDiaResponse adicionarDia(Long planoId, PlanoAlimentarDiaRequest request) {
        validarDia(request);
        PlanoAlimentar plano = buscarPlano(planoId);
        PlanoAlimentarDia dia = new PlanoAlimentarDia();
        dia.setPlanoAlimentar(plano);
        dia.setDiaSemana(request.diaSemana());
        dia.setTitulo(request.titulo().trim());
        return toDiaResponse(planoAlimentarDiaRepository.save(dia));
    }

    @Transactional
    public PlanoAlimentarRefeicaoResponse adicionarRefeicao(Long planoDiaId, PlanoAlimentarRefeicaoRequest request) {
        validarRefeicaoPlanejada(request);
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
        validarAlimentoPlanejado(request);
        PlanoAlimentarRefeicao planoRefeicao = buscarRefeicao(planoRefeicaoId);
        Alimento alimento = alimentoRepository.findById(request.alimentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alimento nao encontrado."));

        PlanoAlimentarRefeicaoAlimento item = new PlanoAlimentarRefeicaoAlimento();
        item.setPlanoAlimentarRefeicao(planoRefeicao);
        item.setAlimento(alimento);
        item.setQuantidade(request.quantidade());
        return toAlimentoResponse(planoAlimentarRefeicaoAlimentoRepository.save(item));
    }

    private void validarPlano(PlanoAlimentarRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do plano alimentar precisam ser informados.");
        }
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("O nome do plano alimentar e obrigatorio.");
        }
        if (request.dataInicio() != null && request.dataFim() != null && request.dataFim().isBefore(request.dataInicio())) {
            throw new BusinessException("A data final do plano alimentar nao pode ser anterior a data inicial.");
        }
    }

    private void validarDia(PlanoAlimentarDiaRequest request) {
        if (request == null || request.diaSemana() == null || request.titulo() == null || request.titulo().isBlank()) {
            throw new BusinessException("Os dados do dia do plano alimentar precisam ser informados.");
        }
    }

    private void validarRefeicaoPlanejada(PlanoAlimentarRefeicaoRequest request) {
        if (request == null || request.tipoRefeicao() == null) {
            throw new BusinessException("O tipo da refeicao planejada precisa ser informado.");
        }
    }

    private void validarAlimentoPlanejado(PlanoAlimentarRefeicaoAlimentoRequest request) {
        if (request == null || request.quantidade() == null || request.quantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("A quantidade do alimento planejado deve ser maior que zero.");
        }
    }

    private void sincronizarPlanoPrincipal(PlanoAlimentar plano) {
        if (!Boolean.TRUE.equals(plano.getPrincipal())) {
            return;
        }
        List<PlanoAlimentar> outrosPlanosPrincipais = planoAlimentarRepository.findAllByUsuarioIdAndPrincipalTrueAndIdNot(
                plano.getUsuario().getId(),
                plano.getId()
        );
        for (PlanoAlimentar outroPlano : outrosPlanosPrincipais) {
            outroPlano.setPrincipal(Boolean.FALSE);
            planoAlimentarRepository.save(outroPlano);
        }
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
                plano.getPrincipal(),
                plano.getDataInicio(),
                plano.getDataFim(),
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

    private RegistroDiarioResponse toRegistroDiarioResponse(RegistroDiario registro) {
        return new RegistroDiarioResponse(
                registro.getId(),
                registro.getUsuario().getId(),
                registro.getPlanoAlimentar() != null ? registro.getPlanoAlimentar().getId() : null,
                registro.getDataReferencia(),
                registro.getRealizouTreino(),
                registro.getTipoTreino(),
                registro.getPeso(),
                registro.getProteinaConsumida(),
                registro.getBateuProteina(),
                registro.getAlimentacaoAlinhada(),
                registro.getHorasSono(),
                registro.getHouveProgressao(),
                registro.getDescricaoProgressao(),
                registro.getMotivacao(),
                registro.getObservacao()
        );
    }
}
