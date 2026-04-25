package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoMedalha;
import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MedalhaRequest(
        @NotBlank(message = "Nome da medalha e obrigatorio.")
        String nome,
        String descricao,
        @NotNull(message = "Tipo da medalha e obrigatorio.")
        TipoMedalha tipo,
        @NotNull(message = "Tipo da regra e obrigatorio.")
        TipoRegraGamificacao tipoRegra,
        @NotNull(message = "Valor meta e obrigatorio.")
        BigDecimal valorMeta,
        String valorReferencia,
        Boolean ativo
) {
}
