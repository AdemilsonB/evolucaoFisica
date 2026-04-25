package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.Objetivo;

import java.math.BigDecimal;

public record MetaAtletaResponse(
        Long id,
        Long usuarioId,
        Objetivo objetivoPrincipal,
        Integer frequenciaSemanalMeta,
        BigDecimal proteinaDiariaMeta,
        BigDecimal caloriaDiariaMeta,
        String observacao
) {
}
