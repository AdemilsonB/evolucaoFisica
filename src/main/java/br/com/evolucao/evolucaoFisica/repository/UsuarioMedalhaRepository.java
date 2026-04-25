package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.UsuarioMedalha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioMedalhaRepository extends JpaRepository<UsuarioMedalha, Long> {
    Optional<UsuarioMedalha> findByUsuarioIdAndMedalhaId(Long usuarioId, Long medalhaId);

    List<UsuarioMedalha> findAllByUsuarioIdOrderByMedalhaNomeAsc(Long usuarioId);
}
