package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FinalizacaoRegistroTreinoRequest(
        @NotNull(message = "Data de finalizacao e obrigatoria.")
        LocalDateTime finalizadoEm,
        String observacao,
        MotivacaoRegistro motivacao
) {
}
