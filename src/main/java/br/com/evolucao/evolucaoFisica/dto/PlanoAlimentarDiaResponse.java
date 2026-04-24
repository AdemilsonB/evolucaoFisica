package br.com.evolucao.evolucaoFisica.dto;

import java.time.DayOfWeek;
import java.util.List;

public record PlanoAlimentarDiaResponse(
        Long id,
        DayOfWeek diaSemana,
        String titulo,
        List<PlanoAlimentarRefeicaoResponse> refeicoes
) {
}
