package br.com.evolucao.evolucaoFisica.dto;

import java.time.LocalDateTime;

public record XpTransacaoResponse(
        Long id,
        Integer valor,
        String descricao,
        String referenciaUnica,
        LocalDateTime criadoEm
) {
}
