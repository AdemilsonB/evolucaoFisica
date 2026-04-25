package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.NivelExperiencia;
import br.com.evolucao.evolucaoFisica.enumeration.Objetivo;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OnboardingRequest(
        @NotNull(message = "Objetivo e obrigatorio.")
        Objetivo objetivo,
        @NotNull(message = "Peso atual e obrigatorio.")
        BigDecimal pesoAtual,
        BigDecimal altura,
        @NotNull(message = "Nivel de experiencia e obrigatorio.")
        NivelExperiencia nivelExperiencia,
        Integer frequenciaSemanalMeta,
        BigDecimal proteinaDiariaMeta,
        BigDecimal caloriaDiariaMeta,
        String observacaoMeta
) {
}
