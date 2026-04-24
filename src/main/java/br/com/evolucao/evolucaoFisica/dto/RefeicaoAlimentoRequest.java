package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RefeicaoAlimentoRequest(
        @NotNull(message = "Alimento e obrigatorio.")
        Long alimentoId,
        @NotNull(message = "Quantidade e obrigatoria.")
        BigDecimal quantidade
) {
}
