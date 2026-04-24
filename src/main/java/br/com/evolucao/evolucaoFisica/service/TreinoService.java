package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.TreinoExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.TreinoExercicioResponse;
import br.com.evolucao.evolucaoFisica.dto.TreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.TreinoResponse;
import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import br.com.evolucao.evolucaoFisica.entity.Treino;
import br.com.evolucao.evolucaoFisica.entity.TreinoExercicio;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.ExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.TreinoExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.TreinoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TreinoService {

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
        Treino treino = new Treino();
        preencherTreino(treino, request);
        return toResponse(treinoRepository.save(treino));
    }

    public List<TreinoResponse> listarPorUsuario(Long usuarioId) {
        return treinoRepository.findAllByUsuarioIdOrderByDataTreinoDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TreinoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public TreinoResponse atualizar(Long id, TreinoRequest request) {
        Treino treino = buscarEntidade(id);
        preencherTreino(treino, request);
        return toResponse(treinoRepository.save(treino));
    }

    @Transactional
    public void excluir(Long id) {
        treinoRepository.delete(buscarEntidade(id));
    }

    @Transactional
    public TreinoExercicioResponse adicionarExercicio(Long treinoId, TreinoExercicioRequest request) {
        Treino treino = buscarEntidade(treinoId);
        Exercicio exercicio = exercicioRepository.findById(request.exercicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercicio nao encontrado."));

        TreinoExercicio treinoExercicio = new TreinoExercicio();
        treinoExercicio.setTreino(treino);
        treinoExercicio.setExercicio(exercicio);
        treinoExercicio.setSeries(request.series());
        treinoExercicio.setRepeticoes(request.repeticoes());
        treinoExercicio.setCarga(request.carga());
        treinoExercicio.setDificuldade(request.dificuldade());

        return toResponse(treinoExercicioRepository.save(treinoExercicio));
    }

    public Treino buscarEntidade(Long id) {
        return treinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treino nao encontrado."));
    }

    private void preencherTreino(Treino treino, TreinoRequest request) {
        treino.setNome(request.nome());
        treino.setDescricao(request.descricao());
        treino.setTipoTreino(request.tipoTreino());
        treino.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        treino.setDataTreino(request.dataTreino());
    }

    private TreinoResponse toResponse(Treino treino) {
        List<TreinoExercicioResponse> exercicios = treinoExercicioRepository.findAllByTreinoIdOrderByIdAsc(treino.getId())
                .stream()
                .map(this::toResponse)
                .toList();

        return new TreinoResponse(
                treino.getId(),
                treino.getNome(),
                treino.getDescricao(),
                treino.getTipoTreino(),
                treino.getUsuario().getId(),
                treino.getDataTreino(),
                exercicios
        );
    }

    private TreinoExercicioResponse toResponse(TreinoExercicio treinoExercicio) {
        return new TreinoExercicioResponse(
                treinoExercicio.getId(),
                treinoExercicio.getExercicio().getId(),
                treinoExercicio.getExercicio().getNome(),
                treinoExercicio.getSeries(),
                treinoExercicio.getRepeticoes(),
                treinoExercicio.getCarga(),
                treinoExercicio.getDificuldade()
        );
    }
}
