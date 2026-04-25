package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.PerfilGamificacaoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerfilGamificacaoUsuarioRepository extends JpaRepository<PerfilGamificacaoUsuario, Long> {
    Optional<PerfilGamificacaoUsuario> findByUsuarioId(Long usuarioId);

    List<PerfilGamificacaoUsuario> findTop20ByOrderByXpTotalDescNivelAtualDesc();

    List<PerfilGamificacaoUsuario> findAllByUsuarioIdInOrderByXpTotalDescNivelAtualDesc(List<Long> usuarioIds);
}
