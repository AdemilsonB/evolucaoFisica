package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Medalha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedalhaRepository extends JpaRepository<Medalha, Long> {
    List<Medalha> findAllByAtivoTrueOrderByNomeAsc();
}
