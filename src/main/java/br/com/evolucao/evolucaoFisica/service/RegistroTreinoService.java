package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.AbortarRegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.FinalizacaoRegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.InicioRegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroExercicioResponse;
import br.com.evolucao.evolucaoFisica.dto.RegistroTreinoRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroTreinoResponse;
import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import br.com.evolucao.evolucaoFisica.entity.RegistroExercicio;
import br.com.evolucao.evolucaoFisica.entity.RegistroTreino;
import br.com.evolucao.evolucaoFisica.entity.TreinoExercicio;
import br.com.evolucao.evolucaoFisica.enumeration.StatusExecucaoTreino;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.ExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroTreinoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RegistroTreinoService {

    private static final Logger log = LoggerFactory.getLogger(RegistroTreinoService.class);

    private final RegistroTreinoRepository registroTreinoRepository;
    private final RegistroExercicioRepository registroExercicioRepository;
    private final ExercicioRepository exercicioRepository;
    private final UsuarioService usuarioService;
    private final TreinoService treinoService;
    private final GamificacaoService gamificacaoService;

    public RegistroTreinoService(
            RegistroTreinoRepository registroTreinoRepository,
            RegistroExercicioRepository registroExercicioRepository,
            ExercicioRepository exercicioRepository,
            UsuarioService usuarioService,
            TreinoService treinoService,
            GamificacaoService gamificacaoService
    ) {
        this.registroTreinoRepository = registroTreinoRepository;
        this.registroExercicioRepository = registroExercicioRepository;
        this.exercicioRepository = exercicioRepository;
        this.usuarioService = usuarioService;
        this.treinoService = treinoService;
        this.gamificacaoService = gamificacaoService;
    }

    @Transactional
    public RegistroTreinoResponse iniciarTreino(RegistroTreinoRequest request) {
        validarPlanejamento(request);
        RegistroTreino registro = new RegistroTreino();
        registro.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        registro.setTreino(treinoService.buscarEntidade(request.treinoId()));
        registro.setPlanejadoPara(request.planejadoPara());
        registro.setObservacao(request.observacao());
        registro.setStatus(StatusExecucaoTreino.PLANEJADO);
        registro.setConcluido(false);

        RegistroTreino salvo = registroTreinoRepository.save(registro);
        semearExecucoesPlanejadas(salvo);
        log.info("Execucao de treino planejada para usuarioId={} registroTreinoId={}", request.usuarioId(), salvo.getId());
        return toResponse(salvo);
    }

    @Transactional
    public RegistroTreinoResponse iniciarExecucao(Long id, InicioRegistroTreinoRequest request) {
        RegistroTreino registro = buscarEntidade(id);
        if (registro.getStatus() == StatusExecucaoTreino.CONCLUIDO) {
            throw new BusinessException("Nao e possivel iniciar um treino ja concluido.");
        }
        if (registro.getStatus() == StatusExecucaoTreino.ABORTADO) {
            throw new BusinessException("Nao e possivel iniciar um treino abortado.");
        }
        registro.setStatus(StatusExecucaoTreino.INICIADO);
        registro.setIniciadoEm(request.iniciadoEm());
        registro.setDataRegistro(request.iniciadoEm());
        RegistroTreino salvo = registroTreinoRepository.save(registro);
        log.info("Execucao de treino iniciada registroTreinoId={}", id);
        return toResponse(salvo);
    }

    @Transactional
    public RegistroExercicioResponse registrarExecucao(Long registroTreinoId, RegistroExercicioRequest request) {
        RegistroTreino registroTreino = buscarEntidade(registroTreinoId);
        if (registroTreino.getStatus() != StatusExecucaoTreino.INICIADO && registroTreino.getStatus() != StatusExecucaoTreino.PLANEJADO) {
            throw new BusinessException("So e permitido registrar execucao para treino planejado ou iniciado.");
        }
        Exercicio exercicio = exercicioRepository.findById(request.exercicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercicio nao encontrado."));

        RegistroExercicio registroExercicio = localizarOuCriarExecucao(registroTreinoId, request, exercicio, registroTreino);
        registroExercicio.setCargaReal(request.cargaReal());
        registroExercicio.setRepeticoesReal(request.repeticoesReal());
        registroExercicio.setConcluido(request.concluido());

        RegistroExercicio salvo = registroExercicioRepository.save(registroExercicio);
        log.info("Execucao registrada para registroTreinoId={} exercicioId={}", registroTreinoId, request.exercicioId());
        return toResponse(salvo);
    }

    @Transactional
    public RegistroTreinoResponse finalizarTreino(Long id, FinalizacaoRegistroTreinoRequest request) {
        RegistroTreino registro = buscarEntidade(id);
        if (registro.getStatus() == StatusExecucaoTreino.CONCLUIDO) {
            log.warn("Tentativa de finalizar treino ja concluido registroTreinoId={}", id);
            return toResponse(registro);
        }
        if (registro.getStatus() == StatusExecucaoTreino.ABORTADO) {
            throw new BusinessException("Nao e possivel finalizar um treino abortado.");
        }
        if (registro.getStatus() == StatusExecucaoTreino.PLANEJADO && registro.getDataRegistro() == null) {
            registro.setIniciadoEm(request.finalizadoEm());
            registro.setDataRegistro(request.finalizadoEm());
        }
        registro.setFinalizadoEm(request.finalizadoEm());
        registro.setObservacao(request.observacao());
        registro.setMotivacao(request.motivacao());
        registro.setStatus(StatusExecucaoTreino.CONCLUIDO);
        registro.setConcluido(true);
        RegistroTreino salvo = registroTreinoRepository.save(registro);
        gamificacaoService.processarTreinoConcluido(salvo);
        log.info("Treino finalizado para usuarioId={} registroTreinoId={}", salvo.getUsuario().getId(), salvo.getId());
        return toResponse(salvo);
    }

    @Transactional
    public RegistroTreinoResponse abortarTreino(Long id, AbortarRegistroTreinoRequest request) {
        RegistroTreino registro = buscarEntidade(id);
        if (registro.getStatus() == StatusExecucaoTreino.CONCLUIDO) {
            throw new BusinessException("Nao e possivel abortar um treino ja concluido.");
        }
        registro.setAbortadoEm(request.abortadoEm());
        registro.setObservacao(request.observacao());
        registro.setStatus(StatusExecucaoTreino.ABORTADO);
        registro.setConcluido(false);
        RegistroTreino salvo = registroTreinoRepository.save(registro);
        log.info("Execucao de treino abortada registroTreinoId={}", id);
        return toResponse(salvo);
    }

    public List<RegistroTreinoResponse> listar(Long usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return registroTreinoRepository
                .buscarPorPeriodo(usuarioId, dataInicio, dataFim)
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
        return registroTreinoRepository.findWithTreinoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de treino nao encontrado."));
    }

    private void validarPlanejamento(RegistroTreinoRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do planejamento do treino precisam ser informados.");
        }
        if (request.planejadoPara() == null) {
            throw new BusinessException("A data planejada do treino precisa ser informada.");
        }
    }

    private void semearExecucoesPlanejadas(RegistroTreino registroTreino) {
        List<TreinoExercicio> exerciciosPlanejados = treinoService.listarExerciciosPlanejados(registroTreino.getTreino().getId());
        for (TreinoExercicio treinoExercicio : exerciciosPlanejados) {
            RegistroExercicio registroExercicio = new RegistroExercicio();
            registroExercicio.setRegistroTreino(registroTreino);
            registroExercicio.setTreinoExercicio(treinoExercicio);
            registroExercicio.setExercicio(treinoExercicio.getExercicio());
            registroExercicio.setConcluido(false);
            registroExercicioRepository.save(registroExercicio);
        }
    }

    private RegistroExercicio localizarOuCriarExecucao(
            Long registroTreinoId,
            RegistroExercicioRequest request,
            Exercicio exercicio,
            RegistroTreino registroTreino
    ) {
        if (request.treinoExercicioId() != null) {
            TreinoExercicio treinoExercicio = treinoService.buscarTreinoExercicio(registroTreino.getTreino().getId(), request.treinoExercicioId());
            return registroExercicioRepository.findByRegistroTreinoIdAndTreinoExercicioId(registroTreinoId, request.treinoExercicioId())
                    .map(registroExistente -> {
                        registroExistente.setExercicio(exercicio);
                        registroExistente.setTreinoExercicio(treinoExercicio);
                        return registroExistente;
                    })
                    .orElseGet(() -> {
                        RegistroExercicio novo = new RegistroExercicio();
                        novo.setRegistroTreino(registroTreino);
                        novo.setTreinoExercicio(treinoExercicio);
                        novo.setExercicio(exercicio);
                        return novo;
                    });
        }

        RegistroExercicio registroExercicio = new RegistroExercicio();
        registroExercicio.setRegistroTreino(registroTreino);
        registroExercicio.setExercicio(exercicio);
        return registroExercicio;
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
                registro.getPlanejadoPara(),
                registro.getIniciadoEm(),
                registro.getDataRegistro(),
                registro.getAbortadoEm(),
                registro.getFinalizadoEm(),
                registro.getStatus(),
                registro.getObservacao(),
                registro.getMotivacao(),
                registro.isConcluido(),
                execucoes
        );
    }

    private RegistroExercicioResponse toResponse(RegistroExercicio registroExercicio) {
        return new RegistroExercicioResponse(
                registroExercicio.getId(),
                registroExercicio.getTreinoExercicio() != null ? registroExercicio.getTreinoExercicio().getId() : null,
                registroExercicio.getExercicio().getId(),
                registroExercicio.getExercicio().getNome(),
                registroExercicio.getTreinoExercicio() != null ? registroExercicio.getTreinoExercicio().getOrdem() : null,
                registroExercicio.getTreinoExercicio() != null ? registroExercicio.getTreinoExercicio().getSeries() : null,
                registroExercicio.getTreinoExercicio() != null ? registroExercicio.getTreinoExercicio().getRepeticoes() : null,
                registroExercicio.getTreinoExercicio() != null ? registroExercicio.getTreinoExercicio().getCarga() : null,
                registroExercicio.getCargaReal(),
                registroExercicio.getRepeticoesReal(),
                registroExercicio.isConcluido()
        );
    }
}
