package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro;

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
        MotivacaoRegistro motivacao,
        boolean concluido,
        List<RegistroExercicioResponse> execucoes
) {
}
