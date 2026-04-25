package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    List<Exercicio> findAllByOrderByNomeAsc();

    @Query("""
            select e
            from Exercicio e
            where (:grupoMuscular is null or lower(e.grupoMuscular) = lower(:grupoMuscular))
              and (:equipamento is null or lower(e.equipamento) = lower(:equipamento))
              and (:nome is null or lower(e.nome) like lower(concat('%', :nome, '%')))
              and (:ativo is null or e.ativo = :ativo)
            order by e.nome asc
            """)
    List<Exercicio> buscar(String nome, String grupoMuscular, String equipamento, Boolean ativo);
}
