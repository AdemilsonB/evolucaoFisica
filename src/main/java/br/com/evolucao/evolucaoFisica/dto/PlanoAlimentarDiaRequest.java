package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;

public record PlanoAlimentarDiaRequest(
        @NotNull(message = "Dia da semana e obrigatorio.")
        DayOfWeek diaSemana,
        @NotBlank(message = "Titulo do dia e obrigatorio.")
        String titulo
) {
}
