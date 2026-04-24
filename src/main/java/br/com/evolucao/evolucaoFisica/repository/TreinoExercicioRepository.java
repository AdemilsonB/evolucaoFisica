package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.TreinoExercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreinoExercicioRepository extends JpaRepository<TreinoExercicio, Long> {
    List<TreinoExercicio> findAllByTreinoIdOrderByIdAsc(Long treinoId);
}
