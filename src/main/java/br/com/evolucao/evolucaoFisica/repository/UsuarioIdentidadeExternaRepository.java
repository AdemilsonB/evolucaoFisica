package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.UsuarioIdentidadeExterna;
import br.com.evolucao.evolucaoFisica.enumeration.ProvedorAutenticacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioIdentidadeExternaRepository extends JpaRepository<UsuarioIdentidadeExterna, Long> {
    boolean existsByProvedorAndIdentificadorExterno(ProvedorAutenticacao provedor, String identificadorExterno);

    Optional<UsuarioIdentidadeExterna> findByProvedorAndIdentificadorExterno(ProvedorAutenticacao provedor, String identificadorExterno);

    List<UsuarioIdentidadeExterna> findAllByUsuarioIdOrderByCriadoEmAsc(Long usuarioId);
}
