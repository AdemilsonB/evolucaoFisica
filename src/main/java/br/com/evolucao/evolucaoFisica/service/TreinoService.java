package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.TreinoExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.TreinoExercicioResponse;
import br.com.evolucao.evolucaoFisica.dto.TreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.TreinoResponse;
import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import br.com.evolucao.evolucaoFisica.entity.Treino;
import br.com.evolucao.evolucaoFisica.entity.TreinoExercicio;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.ExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.TreinoExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.TreinoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TreinoService {

    private static final Logger log = LoggerFactory.getLogger(TreinoService.class);

    private final TreinoRepository treinoRepository;
    private final TreinoExercicioRepository treinoExercicioRepository;
    private final ExercicioRepository exercicioRepository;
    private final UsuarioService usuarioService;

    public TreinoService(
            TreinoRepository treinoRepository,
            TreinoExercicioRepository treinoExercicioRepository,
            ExercicioRepository exercicioRepository,
            UsuarioService usuarioService
    ) {
        this.treinoRepository = treinoRepository;
        this.treinoExercicioRepository = treinoExercicioRepository;
        this.exercicioRepository = exercicioRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public TreinoResponse criarTreino(TreinoRequest request) {
        validarTreino(request);
        Treino treino = new Treino();
        preencherTreino(treino, request);
        log.info("Treino planejado criado usuarioId={} nome={}", request.usuarioId(), request.nome());
        return toResponse(treinoRepository.save(treino));
    }

    public List<TreinoResponse> listarPorUsuario(Long usuarioId) {
        return treinoRepository.findAllByUsuarioIdOrderByDataTreinoDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TreinoResponse> listarAgendaSemanal(Long usuarioId) {
        return treinoRepository.findAllByUsuarioIdAndAtivoTrueOrderByDiaSemanaAscDataTreinoAsc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TreinoResponse> listarPorDiaSemana(Long usuarioId, DayOfWeek diaSemana) {
        return treinoRepository.findAllByUsuarioIdAndDiaSemanaAndAtivoTrueOrderByDataTreinoAsc(usuarioId, diaSemana)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TreinoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public TreinoResponse atualizar(Long id, TreinoRequest request) {
        validarTreino(request);
        Treino treino = buscarEntidade(id);
        preencherTreino(treino, request);
        log.info("Treino atualizado treinoId={}", id);
        return toResponse(treinoRepository.save(treino));
    }

    @Transactional
    public void excluir(Long id) {
        treinoRepository.delete(buscarEntidade(id));
    }

    @Transactional
    public TreinoExercicioResponse adicionarExercicio(Long treinoId, TreinoExercicioRequest request) {
        validarTreinoExercicio(request);
        Treino treino = buscarEntidade(treinoId);
        Exercicio exercicio = exercicioRepository.findById(request.exercicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercicio nao encontrado."));

        TreinoExercicio treinoExercicio = new TreinoExercicio();
        treinoExercicio.setTreino(treino);
        treinoExercicio.setExercicio(exercicio);
        treinoExercicio.setOrdem(request.ordem());
        treinoExercicio.setSeries(request.series());
        treinoExercicio.setRepeticoes(request.repeticoes());
        treinoExercicio.setCarga(request.carga());
        treinoExercicio.setDificuldade(request.dificuldade());

        log.info("Exercicio adicionado ao treino treinoId={} exercicioId={}", treinoId, request.exercicioId());
        return toResponse(treinoExercicioRepository.save(treinoExercicio));
    }

    @Transactional
    public void removerExercicio(Long treinoId, Long treinoExercicioId) {
        TreinoExercicio treinoExercicio = treinoExercicioRepository.findById(treinoExercicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercicio do treino nao encontrado."));

        if (!treinoExercicio.getTreino().getId().equals(treinoId)) {
            throw new ResourceNotFoundException("Exercicio do treino nao pertence ao treino informado.");
        }

        treinoExercicioRepository.delete(treinoExercicio);
    }

    public Treino buscarEntidade(Long id) {
        return treinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treino nao encontrado."));
    }

    public List<TreinoExercicio> listarExerciciosPlanejados(Long treinoId) {
        buscarEntidade(treinoId);
        return treinoExercicioRepository.findAllByTreinoIdOrderByOrdemAscIdAsc(treinoId);
    }

    public TreinoExercicio buscarTreinoExercicio(Long treinoId, Long treinoExercicioId) {
        return treinoExercicioRepository.findByTreinoIdAndId(treinoId, treinoExercicioId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercicio planejado nao encontrado para o treino informado."));
    }

    private void preencherTreino(Treino treino, TreinoRequest request) {
        treino.setNome(request.nome().trim());
        treino.setDescricao(request.descricao());
        treino.setObservacoes(request.observacoes());
        treino.setTipoTreino(request.tipoTreino());
        treino.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        treino.setDiaSemana(request.diaSemana());
        treino.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
        treino.setPublico(request.publico() == null ? Boolean.FALSE : request.publico());
        treino.setRecorrente(request.recorrente() == null ? Boolean.TRUE : request.recorrente());
        treino.setDataTreino(request.dataTreino());
    }

    private void validarTreino(TreinoRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do treino precisam ser informados.");
        }
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("O nome do treino e obrigatorio.");
        }
        if (request.tipoTreino() == null) {
            throw new BusinessException("O tipo do treino e obrigatorio.");
        }
        if (request.dataTreino() == null) {
            throw new BusinessException("A data base do treino planejado e obrigatoria.");
        }
    }

    private void validarTreinoExercicio(TreinoExercicioRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do exercicio planejado precisam ser informados.");
        }
        if (request.ordem() == null || request.ordem() <= 0) {
            throw new BusinessException("A ordem do exercicio deve ser maior que zero.");
        }
        if (request.series() == null || request.series() <= 0) {
            throw new BusinessException("A quantidade de series deve ser maior que zero.");
        }
        if (request.repeticoes() == null || request.repeticoes() <= 0) {
            throw new BusinessException("A quantidade de repeticoes deve ser maior que zero.");
        }
    }

    private TreinoResponse toResponse(Treino treino) {
        List<TreinoExercicioResponse> exercicios = treinoExercicioRepository.findAllByTreinoIdOrderByOrdemAscIdAsc(treino.getId())
                .stream()
                .map(this::toResponse)
                .toList();

        return new TreinoResponse(
                treino.getId(),
                treino.getNome(),
                treino.getDescricao(),
                treino.getObservacoes(),
                treino.getTipoTreino(),
                treino.getUsuario().getId(),
                treino.getDiaSemana(),
                treino.getAtivo(),
                treino.getPublico(),
                treino.getRecorrente(),
                treino.getDataTreino(),
                exercicios
        );
    }

    private TreinoExercicioResponse toResponse(TreinoExercicio treinoExercicio) {
        return new TreinoExercicioResponse(
                treinoExercicio.getId(),
                treinoExercicio.getExercicio().getId(),
                treinoExercicio.getExercicio().getNome(),
                treinoExercicio.getOrdem(),
                treinoExercicio.getSeries(),
                treinoExercicio.getRepeticoes(),
                treinoExercicio.getCarga(),
                treinoExercicio.getDificuldade()
        );
    }
}
