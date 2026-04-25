package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarDia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface PlanoAlimentarDiaRepository extends JpaRepository<PlanoAlimentarDia, Long> {
    List<PlanoAlimentarDia> findAllByPlanoAlimentarIdOrderByDiaSemanaAsc(Long planoAlimentarId);

    List<PlanoAlimentarDia> findAllByPlanoAlimentarUsuarioIdAndDiaSemanaOrderByTituloAsc(Long usuarioId, DayOfWeek diaSemana);

    Optional<PlanoAlimentarDia> findByPlanoAlimentarIdAndDiaSemana(Long planoAlimentarId, DayOfWeek diaSemana);
}
