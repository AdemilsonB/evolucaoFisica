package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;

public record UsuarioMissaoSemanalResponse(
        Long missaoId,
        String nome,
        String descricao,
        BigDecimal progresso,
        BigDecimal metaValor,
        Boolean concluida,
        Integer totalConclusoes,
        Integer xpRecompensa
) {
}
