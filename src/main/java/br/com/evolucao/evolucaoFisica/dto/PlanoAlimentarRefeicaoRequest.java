package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRefeicao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record PlanoAlimentarRefeicaoRequest(
        @NotNull(message = "Tipo da refeicao e obrigatorio.")
        TipoRefeicao tipoRefeicao,
        LocalTime horarioSugerido,
        String observacao
) {
}
