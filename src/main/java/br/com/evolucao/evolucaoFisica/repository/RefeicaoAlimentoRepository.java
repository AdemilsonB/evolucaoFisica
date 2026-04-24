package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.RefeicaoAlimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefeicaoAlimentoRepository extends JpaRepository<RefeicaoAlimento, Long> {
    List<RefeicaoAlimento> findAllByRefeicaoIdOrderByIdAsc(Long refeicaoId);
}
