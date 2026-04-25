package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.MetaAtleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetaAtletaRepository extends JpaRepository<MetaAtleta, Long> {
    Optional<MetaAtleta> findByUsuarioId(Long usuarioId);
}
