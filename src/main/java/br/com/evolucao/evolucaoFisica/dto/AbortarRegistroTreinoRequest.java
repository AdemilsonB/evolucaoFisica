package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AbortarRegistroTreinoRequest(
        @NotNull(message = "A data de aborto e obrigatoria.")
        LocalDateTime abortadoEm,
        String observacao
) {
}
