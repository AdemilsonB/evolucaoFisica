package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;

public record RegistroExercicioResponse(
        Long id,
        Long exercicioId,
        String exercicioNome,
        BigDecimal cargaReal,
        Integer repeticoesReal,
        boolean concluido
) {
}
