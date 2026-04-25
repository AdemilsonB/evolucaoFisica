package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;

import java.math.BigDecimal;

public record XpRegraResponse(
        Long id,
        String nome,
        TipoRegraGamificacao tipoRegra,
        Integer xpConcedido,
        BigDecimal percentualBonus,
        String descricao,
        Boolean ativo
) {
}
