package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AtualizarPesoRequest(
        @NotNull(message = "Peso atual e obrigatorio.")
        BigDecimal pesoAtual
) {
}
