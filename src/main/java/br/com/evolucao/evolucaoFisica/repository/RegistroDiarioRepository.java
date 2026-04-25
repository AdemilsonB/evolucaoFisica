package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.RegistroDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroDiarioRepository extends JpaRepository<RegistroDiario, Long> {
    Optional<RegistroDiario> findByUsuarioIdAndDataReferencia(Long usuarioId, LocalDate dataReferencia);

    List<RegistroDiario> findAllByUsuarioIdOrderByDataReferenciaDesc(Long usuarioId);

    List<RegistroDiario> findAllByUsuarioIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(Long usuarioId, LocalDate inicio, LocalDate fim);

    long countByUsuarioIdAndRealizouTreinoTrue(Long usuarioId);

    long countByUsuarioIdAndRealizouTreinoTrueAndMotivacao(Long usuarioId, br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro motivacao);

    long countByUsuarioIdAndHouveProgressaoTrue(Long usuarioId);

    long countByUsuarioIdAndAlimentacaoAlinhadaTrue(Long usuarioId);
}
