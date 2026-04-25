package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;

public record RegistroExercicioResponse(
        Long id,
        Long treinoExercicioId,
        Long exercicioId,
        String exercicioNome,
        Integer ordemPlanejada,
        Integer seriesPlanejadas,
        Integer repeticoesPlanejadas,
        BigDecimal cargaPlanejada,
        BigDecimal cargaReal,
        Integer repeticoesReal,
        boolean concluido
) {
}
