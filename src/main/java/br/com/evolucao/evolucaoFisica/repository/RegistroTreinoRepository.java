package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.RegistroTreino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistroTreinoRepository extends JpaRepository<RegistroTreino, Long> {
    List<RegistroTreino> findAllByUsuarioIdAndDataRegistroBetweenOrderByDataRegistroDesc(
            Long usuarioId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

    List<RegistroTreino> findAllByUsuarioIdAndConcluidoTrueOrderByDataRegistroAsc(Long usuarioId);

    List<RegistroTreino> findAllByUsuarioIdAndConcluidoTrueAndDataRegistroBetweenOrderByDataRegistroAsc(
            Long usuarioId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );
}
