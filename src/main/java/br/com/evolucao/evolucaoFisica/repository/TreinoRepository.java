package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Treino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface TreinoRepository extends JpaRepository<Treino, Long> {
    List<Treino> findAllByUsuarioIdOrderByDataTreinoDesc(Long usuarioId);

    List<Treino> findAllByUsuarioIdAndAtivoTrueOrderByDiaSemanaAscDataTreinoAsc(Long usuarioId);

    List<Treino> findAllByUsuarioIdAndDiaSemanaAndAtivoTrueOrderByDataTreinoAsc(Long usuarioId, DayOfWeek diaSemana);
}
