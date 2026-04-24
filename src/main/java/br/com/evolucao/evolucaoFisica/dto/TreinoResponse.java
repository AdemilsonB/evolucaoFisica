package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoTreino;

import java.time.LocalDateTime;
import java.util.List;

public record TreinoResponse(
        Long id,
        String nome,
        String descricao,
        TipoTreino tipoTreino,
        Long usuarioId,
        LocalDateTime dataTreino,
        List<TreinoExercicioResponse> exercicios
) {
}
