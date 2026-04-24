package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Seguidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeguidorRepository extends JpaRepository<Seguidor, Long> {
    Optional<Seguidor> findBySeguidorIdAndSeguidoId(Long seguidorId, Long seguidoId);

    List<Seguidor> findAllBySeguidoIdAndAtivoTrue(Long seguidoId);

    List<Seguidor> findAllBySeguidorIdAndAtivoTrue(Long seguidorId);
}
