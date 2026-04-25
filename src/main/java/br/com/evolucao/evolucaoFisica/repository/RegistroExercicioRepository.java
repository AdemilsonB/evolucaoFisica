package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.RegistroExercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RegistroExercicioRepository extends JpaRepository<RegistroExercicio, Long> {
    @EntityGraph(attributePaths = {"exercicio", "treinoExercicio", "treinoExercicio.exercicio"})
    List<RegistroExercicio> findAllByRegistroTreinoIdOrderByIdAsc(Long registroTreinoId);

    Optional<RegistroExercicio> findByRegistroTreinoIdAndTreinoExercicioId(Long registroTreinoId, Long treinoExercicioId);

    @Query("""
            select max(re.cargaReal)
            from RegistroExercicio re
            where re.registroTreino.usuario.id = :usuarioId
              and lower(re.exercicio.nome) = lower(:nomeExercicio)
            """)
    BigDecimal findMaxCargaByUsuarioAndNomeExercicio(Long usuarioId, String nomeExercicio);
}
