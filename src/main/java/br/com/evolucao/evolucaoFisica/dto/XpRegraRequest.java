package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record XpRegraRequest(
        @NotBlank(message = "Nome da regra e obrigatorio.")
        String nome,
        @NotNull(message = "Tipo da regra e obrigatorio.")
        TipoRegraGamificacao tipoRegra,
        Integer xpConcedido,
        BigDecimal percentualBonus,
        String descricao,
        Boolean ativo
) {
}
