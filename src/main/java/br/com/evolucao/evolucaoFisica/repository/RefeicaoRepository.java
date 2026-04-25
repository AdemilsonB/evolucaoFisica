package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Refeicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RefeicaoRepository extends JpaRepository<Refeicao, Long> {
    List<Refeicao> findAllByUsuarioIdAndDataBetweenOrderByDataDesc(
            Long usuarioId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );

    default List<Refeicao> findAllByUsuarioIdAndDataReferencia(Long usuarioId, LocalDate dataReferencia) {
        return findAllByUsuarioIdAndDataBetweenOrderByDataDesc(
                usuarioId,
                dataReferencia.atStartOfDay(),
                dataReferencia.plusDays(1).atStartOfDay().minusNanos(1)
        );
    }
}
