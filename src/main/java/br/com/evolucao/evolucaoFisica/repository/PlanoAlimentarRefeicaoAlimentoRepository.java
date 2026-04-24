package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentarRefeicaoAlimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanoAlimentarRefeicaoAlimentoRepository extends JpaRepository<PlanoAlimentarRefeicaoAlimento, Long> {
    List<PlanoAlimentarRefeicaoAlimento> findAllByPlanoAlimentarRefeicaoIdOrderByIdAsc(Long planoAlimentarRefeicaoId);
}
