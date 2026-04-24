package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.Objetivo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        BigDecimal pesoAtual,
        BigDecimal altura,
        Objetivo objetivo,
        Boolean ativo,
        LocalDateTime dataCriacao
) {
}
