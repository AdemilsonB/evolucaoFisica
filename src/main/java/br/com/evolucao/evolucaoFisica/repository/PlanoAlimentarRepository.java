package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanoAlimentarRepository extends JpaRepository<PlanoAlimentar, Long> {
    List<PlanoAlimentar> findAllByUsuarioIdOrderByNomeAsc(Long usuarioId);

    List<PlanoAlimentar> findAllByUsuarioIdAndPrincipalTrueAndIdNot(Long usuarioId, Long id);

    @Query("""
            select p
            from PlanoAlimentar p
            where p.usuario.id = :usuarioId
              and p.ativo = true
              and p.principal = true
              and (p.dataInicio is null or p.dataInicio <= :dataReferencia)
              and (p.dataFim is null or p.dataFim >= :dataReferencia)
            order by p.id desc
            """)
    List<PlanoAlimentar> findPlanosPrincipaisAtivos(Long usuarioId, LocalDate dataReferencia);

    default Optional<PlanoAlimentar> findPlanoPrincipalAtivo(Long usuarioId, LocalDate dataReferencia) {
        return findPlanosPrincipaisAtivos(usuarioId, dataReferencia).stream().findFirst();
    }
}
