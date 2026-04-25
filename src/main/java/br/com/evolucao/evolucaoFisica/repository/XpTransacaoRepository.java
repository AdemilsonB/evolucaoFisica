package br.com.evolucao.evolucaoFisica.repository;

import br.com.evolucao.evolucaoFisica.entity.XpTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XpTransacaoRepository extends JpaRepository<XpTransacao, Long> {
    boolean existsByUsuarioIdAndReferenciaUnica(Long usuarioId, String referenciaUnica);

    List<XpTransacao> findAllByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}
