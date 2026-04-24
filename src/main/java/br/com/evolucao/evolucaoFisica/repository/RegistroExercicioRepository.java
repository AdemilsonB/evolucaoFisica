package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.RegistroExercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroExercicioRepository extends JpaRepository<RegistroExercicio, Long> {
    List<RegistroExercicio> findAllByRegistroTreinoIdOrderByIdAsc(Long registroTreinoId);
}
