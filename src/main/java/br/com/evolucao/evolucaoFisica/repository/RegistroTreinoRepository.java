package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.RegistroTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroTreinoRepository extends JpaRepository<RegistroTreino, Long> {
    @EntityGraph(attributePaths = {"usuario", "treino"})
    @Query("""
            select rt
            from RegistroTreino rt
            where rt.usuario.id = :usuarioId
              and coalesce(rt.dataRegistro, rt.planejadoPara) between :dataInicio and :dataFim
            order by coalesce(rt.dataRegistro, rt.planejadoPara) desc
            """)
    List<RegistroTreino> buscarPorPeriodo(Long usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim);

    List<RegistroTreino> findAllByUsuarioIdAndConcluidoTrueOrderByDataRegistroAsc(Long usuarioId);

    List<RegistroTreino> findAllByUsuarioIdAndConcluidoTrueAndDataRegistroBetweenOrderByDataRegistroAsc(
            Long usuarioId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

    @EntityGraph(attributePaths = {"usuario", "treino"})
    Optional<RegistroTreino> findWithTreinoById(Long id);
}
