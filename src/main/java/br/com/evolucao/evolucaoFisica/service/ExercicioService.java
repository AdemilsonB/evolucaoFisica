package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.ExercicioRequest;
import br.com.evolucao.evolucaoFisica.dto.ExercicioResponse;
import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.ExercicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;

    public ExercicioService(ExercicioRepository exercicioRepository) {
        this.exercicioRepository = exercicioRepository;
    }

    @Transactional
    public ExercicioResponse criar(ExercicioRequest request) {
        Exercicio exercicio = new Exercicio();
        exercicio.setNome(request.nome());
        exercicio.setGrupoMuscular(request.grupoMuscular());
        exercicio.setDescricao(request.descricao());
        return toResponse(exercicioRepository.save(exercicio));
    }

    public List<ExercicioResponse> listar() {
        return exercicioRepository.findAllByOrderByNomeAsc().stream().map(this::toResponse).toList();
    }

    public ExercicioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public ExercicioResponse atualizar(Long id, ExercicioRequest request) {
        Exercicio exercicio = buscarEntidade(id);
        exercicio.setNome(request.nome());
        exercicio.setGrupoMuscular(request.grupoMuscular());
        exercicio.setDescricao(request.descricao());
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

    private ExercicioResponse toResponse(Exercicio exercicio) {
        return new ExercicioResponse(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getGrupoMuscular(),
                exercicio.getDescricao()
        );
    }
}
