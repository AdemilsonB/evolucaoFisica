package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EvolucaoFisicaResponse(
        Long id,
        Long usuarioId,
        BigDecimal peso,
        BigDecimal percentualGordura,
        BigDecimal massaMagra,
        LocalDateTime dataRegistro
) {
}
