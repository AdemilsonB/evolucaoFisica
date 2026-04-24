package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoTreino;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public record TreinoResponse(
        Long id,
        String nome,
        String descricao,
        String observacoes,
        TipoTreino tipoTreino,
        Long usuarioId,
        DayOfWeek diaSemana,
        Boolean ativo,
        Boolean publico,
        Boolean recorrente,
        LocalDateTime dataTreino,
        List<TreinoExercicioResponse> exercicios
) {
}
