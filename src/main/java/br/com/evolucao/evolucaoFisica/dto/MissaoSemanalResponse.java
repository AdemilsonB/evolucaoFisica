package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;

import java.math.BigDecimal;

public record MissaoSemanalResponse(
        Long id,
        String nome,
        String descricao,
        TipoRegraGamificacao tipoRegra,
        BigDecimal metaValor,
        Integer xpRecompensa,
        Boolean ativo
) {
}
