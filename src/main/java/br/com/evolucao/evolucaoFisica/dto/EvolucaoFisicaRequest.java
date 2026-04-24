package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EvolucaoFisicaRequest(
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        @NotNull(message = "Peso e obrigatorio.")
        BigDecimal peso,
        BigDecimal percentualGordura,
        BigDecimal massaMagra,
        @NotNull(message = "Data do registro e obrigatoria.")
        LocalDateTime dataRegistro
) {
}
