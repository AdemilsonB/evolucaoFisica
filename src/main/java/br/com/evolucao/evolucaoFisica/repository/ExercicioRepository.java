package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    List<Exercicio> findAllByOrderByNomeAsc();
}
