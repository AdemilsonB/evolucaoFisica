package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarRefeicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanoAlimentarRefeicaoRepository extends JpaRepository<PlanoAlimentarRefeicao, Long> {
    List<PlanoAlimentarRefeicao> findAllByPlanoAlimentarDiaIdOrderByHorarioSugeridoAscIdAsc(Long planoAlimentarDiaId);
}
