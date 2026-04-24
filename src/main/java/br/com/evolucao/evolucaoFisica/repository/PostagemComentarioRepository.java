package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PostagemComentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostagemComentarioRepository extends JpaRepository<PostagemComentario, Long> {
    List<PostagemComentario> findAllByPostagemIdAndAtivoTrueOrderByCriadoEmAsc(Long postagemId);
}
