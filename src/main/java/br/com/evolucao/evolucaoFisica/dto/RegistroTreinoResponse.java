package br.com.evolucao.evolucaoFisica.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RegistroTreinoResponse(
        Long id,
        Long usuarioId,
        Long treinoId,
        String treinoNome,
        LocalDateTime dataRegistro,
        LocalDateTime finalizadoEm,
        String observacao,
        boolean concluido,
        List<RegistroExercicioResponse> execucoes
) {
}
