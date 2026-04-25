package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.TreinoExercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface TreinoExercicioRepository extends JpaRepository<TreinoExercicio, Long> {
    @EntityGraph(attributePaths = {"exercicio"})
    List<TreinoExercicio> findAllByTreinoIdOrderByOrdemAscIdAsc(Long treinoId);

    Optional<TreinoExercicio> findByTreinoIdAndId(Long treinoId, Long id);
}
