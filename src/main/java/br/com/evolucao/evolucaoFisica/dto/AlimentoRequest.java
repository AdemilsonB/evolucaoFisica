package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AlimentoRequest(
        @NotBlank(message = "Nome do alimento e obrigatorio.")
        String nome,
        @NotNull(message = "Calorias sao obrigatorias.")
        BigDecimal calorias,
        @NotNull(message = "Proteina e obrigatoria.")
        BigDecimal proteina,
        @NotNull(message = "Carboidrato e obrigatorio.")
        BigDecimal carboidrato,
        @NotNull(message = "Gordura e obrigatoria.")
        BigDecimal gordura,
        @NotNull(message = "Acucares sao obrigatorios.")
        BigDecimal acucares
) {
}
