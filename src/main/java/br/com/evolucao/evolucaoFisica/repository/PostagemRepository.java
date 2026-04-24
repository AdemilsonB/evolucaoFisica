package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
    List<Postagem> findAllByAutorIdAndAtivoTrueOrderByCriadoEmDesc(Long autorId);

    List<Postagem> findAllByGrupoIdAndAtivoTrueOrderByCriadoEmDesc(Long grupoId);

    List<Postagem> findAllByAutorIdInAndAtivoTrueOrderByCriadoEmDesc(List<Long> autores);
}
