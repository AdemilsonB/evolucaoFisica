package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PostagemCurtida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostagemCurtidaRepository extends JpaRepository<PostagemCurtida, Long> {
    Optional<PostagemCurtida> findByPostagemIdAndUsuarioId(Long postagemId, Long usuarioId);

    List<PostagemCurtida> findAllByPostagemId(Long postagemId);
}
