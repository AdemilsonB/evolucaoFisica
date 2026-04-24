package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.Dificuldade;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TreinoExercicioRequest(
        @NotNull(message = "Exercicio e obrigatorio.")
        Long exercicioId,
        @NotNull(message = "Series sao obrigatorias.")
        Integer series,
        @NotNull(message = "Repeticoes sao obrigatorias.")
        Integer repeticoes,
        BigDecimal carga,
        @NotNull(message = "Dificuldade e obrigatoria.")
        Dificuldade dificuldade
) {
}
