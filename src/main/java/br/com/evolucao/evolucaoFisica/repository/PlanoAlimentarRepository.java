package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PlanoAlimentar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanoAlimentarRepository extends JpaRepository<PlanoAlimentar, Long> {
    List<PlanoAlimentar> findAllByUsuarioIdOrderByNomeAsc(Long usuarioId);
}
