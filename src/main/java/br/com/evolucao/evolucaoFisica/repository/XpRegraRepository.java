package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.XpRegra;
import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface XpRegraRepository extends JpaRepository<XpRegra, Long> {
    Optional<XpRegra> findByTipoRegra(TipoRegraGamificacao tipoRegra);

    List<XpRegra> findAllByAtivoTrue();
}
