package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;

public record PlanoAlimentarRefeicaoAlimentoResponse(
        Long id,
        Long alimentoId,
        String alimentoNome,
        BigDecimal quantidade
) {
}
