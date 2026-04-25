package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoMedalha;
import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;

import java.math.BigDecimal;

public record MedalhaResponse(
        Long id,
        String nome,
        String descricao,
        TipoMedalha tipo,
        TipoRegraGamificacao tipoRegra,
        BigDecimal valorMeta,
        String valorReferencia,
        Boolean ativo
) {
}
