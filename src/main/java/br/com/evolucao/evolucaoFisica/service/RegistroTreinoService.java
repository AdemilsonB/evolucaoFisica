package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.FinalizacaoRegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroExercicioResponse;
import br.com.evolucao.evolucaoFisica.dto.RegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroTreinoResponse;
import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import br.com.evolucao.evolucaoFisica.entity.RegistroExercicio;
import br.com.evolucao.evolucaoFisica.entity.RegistroTreino;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.ExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroTreinoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RegistroTreinoService {

    private final RegistroTreinoRepository registroTreinoRepository;
    private final RegistroExercicioRepository registroExercicioRepository;
    private final ExercicioRepository exercicioRepository;
    private final UsuarioService usuarioService;
    private final TreinoService treinoService;

    public RegistroTreinoService(
            RegistroTreinoRepository registroTreinoRepository,
            RegistroExercicioRepository registroExercicioRepository,
            ExercicioRepository exercicioRepository,
            UsuarioService usuarioService,
            TreinoService treinoService
    ) {
        this.registroTreinoRepository = registroTreinoRepository;
        this.registroExercicioRepository = registroExercicioRepository;
        this.exercicioRepository = exercicioRepository;
        this.usuarioService = usuarioService;
        this.treinoService = treinoService;
    }

    @Transactional
    public RegistroTreinoResponse iniciarTreino(RegistroTreinoRequest request) {
        RegistroTreino registro = new RegistroTreino();
        registro.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        registro.setTreino(treinoService.buscarEntidade(request.treinoId()));
        registro.setDataRegistro(request.dataRegistro());
        registro.setObservacao(request.observacao());
        registro.setConcluido(false);

        return toResponse(registroTreinoRepository.save(registro));
    }

    @Transactional
    public RegistroExercicioResponse registrarExecucao(Long registroTreinoId, RegistroExercicioRequest request) {
        RegistroTreino registroTreino = buscarEntidade(registroTreinoId);
        Exercicio exercicio = exercicioRepository.findById(request.exercicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercicio nao encontrado."));

        RegistroExercicio registroExercicio = new RegistroExercicio();
        registroExercicio.setRegistroTreino(registroTreino);
        registroExercicio.setExercicio(exercicio);
        registroExercicio.setCargaReal(request.cargaReal());
        registroExercicio.setRepeticoesReal(request.repeticoesReal());
        registroExercicio.setConcluido(request.concluido());

        return toResponse(registroExercicioRepository.save(registroExercicio));
    }

    @Transactional
    public RegistroTreinoResponse finalizarTreino(Long id, FinalizacaoRegistroTreinoRequest request) {
        RegistroTreino registro = buscarEntidade(id);
        registro.setFinalizadoEm(request.finalizadoEm());
        registro.setObservacao(request.observacao());
        registro.setConcluido(true);

        return toResponse(registroTreinoRepository.save(registro));
    }

    public List<RegistroTreinoResponse> listar(Long usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return registroTreinoRepository
                .findAllByUsuarioIdAndDataRegistroBetweenOrderByDataRegistroDesc(usuarioId, dataInicio, dataFim)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RegistroTreinoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public RegistroTreino buscarEntidadeInterna(Long id) {
        return buscarEntidade(id);
    }

    private RegistroTreino buscarEntidade(Long id) {
        return registroTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de treino nao encontrado."));
    }

    private RegistroTreinoResponse toResponse(RegistroTreino registro) {
        List<RegistroExercicioResponse> execucoes = registroExercicioRepository.findAllByRegistroTreinoIdOrderByIdAsc(registro.getId())
                .stream()
                .map(this::toResponse)
                .toList();

        return new RegistroTreinoResponse(
                registro.getId(),
                registro.getUsuario().getId(),
                registro.getTreino().getId(),
                registro.getTreino().getNome(),
                registro.getDataRegistro(),
                registro.getFinalizadoEm(),
                registro.getObservacao(),
                registro.isConcluido(),
                execucoes
        );
    }

    private RegistroExercicioResponse toResponse(RegistroExercicio registroExercicio) {
        return new RegistroExercicioResponse(
                registroExercicio.getId(),
                registroExercicio.getExercicio().getId(),
                registroExercicio.getExercicio().getNome(),
                registroExercicio.getCargaReal(),
                registroExercicio.getRepeticoesReal(),
                registroExercicio.isConcluido()
        );
    }
}
