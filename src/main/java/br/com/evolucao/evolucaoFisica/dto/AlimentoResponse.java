package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;

public record AlimentoResponse(
        Long id,
        String nome,
        BigDecimal calorias,
        BigDecimal proteina,
        BigDecimal carboidrato,
        BigDecimal gordura,
        BigDecimal acucares
) {
}
