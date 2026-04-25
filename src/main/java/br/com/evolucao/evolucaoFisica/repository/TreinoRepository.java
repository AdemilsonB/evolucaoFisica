package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Treino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.DayOfWeek;
import java.util.List;

public interface TreinoRepository extends JpaRepository<Treino, Long> {
    @EntityGraph(attributePaths = {"usuario"})
    List<Treino> findAllByUsuarioIdOrderByDataTreinoDesc(Long usuarioId);

    @EntityGraph(attributePaths = {"usuario"})
    List<Treino> findAllByUsuarioIdAndAtivoTrueOrderByDiaSemanaAscDataTreinoAsc(Long usuarioId);

    @EntityGraph(attributePaths = {"usuario"})
    List<Treino> findAllByUsuarioIdAndDiaSemanaAndAtivoTrueOrderByDataTreinoAsc(Long usuarioId, DayOfWeek diaSemana);
}
