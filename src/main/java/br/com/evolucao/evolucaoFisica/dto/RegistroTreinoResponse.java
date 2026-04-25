package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro;
import br.com.evolucao.evolucaoFisica.enumeration.StatusExecucaoTreino;

import java.time.LocalDateTime;
import java.util.List;

public record RegistroTreinoResponse(
        Long id,
        Long usuarioId,
        Long treinoId,
        String treinoNome,
        LocalDateTime planejadoPara,
        LocalDateTime iniciadoEm,
        LocalDateTime dataRegistro,
        LocalDateTime abortadoEm,
        LocalDateTime finalizadoEm,
        StatusExecucaoTreino status,
        String observacao,
        MotivacaoRegistro motivacao,
        boolean concluido,
        List<RegistroExercicioResponse> execucoes
) {
}
