package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.EvolucaoFisica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvolucaoFisicaRepository extends JpaRepository<EvolucaoFisica, Long> {
    List<EvolucaoFisica> findAllByUsuarioIdOrderByDataRegistroDesc(Long usuarioId);
}
