package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.GrupoMembro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GrupoMembroRepository extends JpaRepository<GrupoMembro, Long> {
    Optional<GrupoMembro> findByGrupoIdAndUsuarioId(Long grupoId, Long usuarioId);

    List<GrupoMembro> findAllByGrupoIdAndAtivoTrue(Long grupoId);

    List<GrupoMembro> findAllByUsuarioIdAndAtivoTrue(Long usuarioId);
}
