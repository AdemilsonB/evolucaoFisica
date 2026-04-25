package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.UsuarioMissaoSemanal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuarioMissaoSemanalRepository extends JpaRepository<UsuarioMissaoSemanal, Long> {
    Optional<UsuarioMissaoSemanal> findByUsuarioIdAndMissaoIdAndSemanaReferencia(Long usuarioId, Long missaoId, LocalDate semanaReferencia);

    List<UsuarioMissaoSemanal> findAllByUsuarioIdAndSemanaReferenciaOrderByMissaoNomeAsc(Long usuarioId, LocalDate semanaReferencia);

    long countByUsuarioIdAndMissaoIdAndConcluidaTrue(Long usuarioId, Long missaoId);
}
