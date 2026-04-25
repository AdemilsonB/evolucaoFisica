package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;

public record MetaAtletaRequest(
        Integer frequenciaSemanalMeta,
        BigDecimal proteinaDiariaMeta,
        BigDecimal caloriaDiariaMeta,
        String observacao
) {
}
