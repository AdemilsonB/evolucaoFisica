package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InicioRegistroTreinoRequest(
        @NotNull(message = "A data de inicio e obrigatoria.")
        LocalDateTime iniciadoEm
) {
}
