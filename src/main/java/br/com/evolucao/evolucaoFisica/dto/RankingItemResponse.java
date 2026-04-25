package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TierRanking;

public record RankingItemResponse(
        Integer posicao,
        Long usuarioId,
        String username,
        Integer xpTotal,
        Integer nivelAtual,
        TierRanking tierAtual
) {
}
