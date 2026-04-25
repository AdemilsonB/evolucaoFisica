package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.ExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.ExercicioResponse;
import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.ExercicioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExercicioService {

    private static final Logger log = LoggerFactory.getLogger(ExercicioService.class);

    private final ExercicioRepository exercicioRepository;

    public ExercicioService(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    @Transactional
    public ExercicioResponse criar(ExercicioRequest request) {
        validar(request);
        Exercicio exercicio = new Exercicio();
        exercicio.setNome(request.nome().trim());
        exercicio.setGrupoMuscular(request.grupoMuscular().trim());
        exercicio.setEquipamento(normalizar(request.equipamento()));
        exercicio.setDescricao(request.descricao());
        exercicio.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
        log.info("Exercicio criado nome={} grupoMuscular={}", exercicio.getNome(), exercicio.getGrupoMuscular());
        return toResponse(exercicioRepository.save(exercicio));
    }

    public List<ExercicioResponse> listar() {
        return exercicioRepository.findAllByOrderByNomeAsc().stream().map(this::toResponse).toList();
    }

    public List<ExercicioResponse> buscar(String nome, String grupoMuscular, String equipamento, Boolean ativo) {
        return exercicioRepository.buscar(normalizar(nome), normalizar(grupoMuscular), normalizar(equipamento), ativo)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ExercicioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public ExercicioResponse atualizar(Long id, ExercicioRequest request) {
        validar(request);
        Exercicio exercicio = buscarEntidade(id);
        exercicio.setNome(request.nome().trim());
        exercicio.setGrupoMuscular(request.grupoMuscular().trim());
        exercicio.setEquipamento(normalizar(request.equipamento()));
        exercicio.setDescricao(request.descricao());
        exercicio.setAtivo(request.ativo() == null ? exercicio.getAtivo() : request.ativo());
        log.info("Exercicio atualizado exercicioId={}", id);
        return toResponse(exercicioRepository.save(exercicio));
    }

    @Transactional
    public void excluir(Long id) {
        exercicioRepository.delete(buscarEntidade(id));
    }

    public Exercicio buscarEntidade(Long id) {
        return exercicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercicio nao encontrado."));
    }

    private void validar(ExercicioRequest request) {
        if (request == null) {
            throw new BusinessException("Os dados do exercicio precisam ser informados.");
        }
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BusinessException("O nome do exercicio e obrigatorio.");
        }
        if (request.grupoMuscular() == null || request.grupoMuscular().isBlank()) {
            throw new BusinessException("O grupo muscular do exercicio e obrigatorio.");
        }
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private ExercicioResponse toResponse(Exercicio exercicio) {
        return new ExercicioResponse(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getGrupoMuscular(),
                exercicio.getEquipamento(),
                exercicio.getDescricao(),
                exercicio.getAtivo()
        );
    }
}
