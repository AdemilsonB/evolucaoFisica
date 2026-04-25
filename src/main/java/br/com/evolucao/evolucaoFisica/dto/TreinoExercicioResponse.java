package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.Dificuldade;

import java.math.BigDecimal;

public record TreinoExercicioResponse(
        Long id,
        Long exercicioId,
        String exercicioNome,
        Integer ordem,
        Integer series,
        Integer repeticoes,
        BigDecimal carga,
        Dificuldade dificuldade
) {
}
