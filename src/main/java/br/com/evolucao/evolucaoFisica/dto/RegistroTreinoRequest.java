package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegistroTreinoRequest(
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        @NotNull(message = "Treino e obrigatorio.")
        Long treinoId,
        @NotNull(message = "Data de registro e obrigatoria.")
        LocalDateTime planejadoPara,
        String observacao
) {
}
