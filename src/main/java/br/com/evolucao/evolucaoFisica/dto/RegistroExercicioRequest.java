package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RegistroExercicioRequest(
        @NotNull(message = "Exercicio e obrigatorio.")
        Long exercicioId,
        BigDecimal cargaReal,
        @NotNull(message = "Repeticoes realizadas sao obrigatorias.")
        Integer repeticoesReal,
        @NotNull(message = "Indicador de conclusao e obrigatorio.")
        Boolean concluido
) {
}
