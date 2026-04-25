package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MissaoSemanalRequest(
        @NotBlank(message = "Nome da missao e obrigatorio.")
        String nome,
        String descricao,
        @NotNull(message = "Tipo da regra e obrigatorio.")
        TipoRegraGamificacao tipoRegra,
        @NotNull(message = "Meta da missao e obrigatoria.")
        BigDecimal metaValor,
        @NotNull(message = "XP da missao e obrigatorio.")
        Integer xpRecompensa,
        Boolean ativo
) {
}
