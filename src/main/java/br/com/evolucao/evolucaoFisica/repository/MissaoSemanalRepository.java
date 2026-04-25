package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.MissaoSemanal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissaoSemanalRepository extends JpaRepository<MissaoSemanal, Long> {
    List<MissaoSemanal> findAllByAtivoTrueOrderByNomeAsc();
}
