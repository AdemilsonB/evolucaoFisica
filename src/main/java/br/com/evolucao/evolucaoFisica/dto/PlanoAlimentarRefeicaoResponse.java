package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRefeicao;

import java.time.LocalTime;
import java.util.List;

public record PlanoAlimentarRefeicaoResponse(
        Long id,
        TipoRefeicao tipoRefeicao,
        LocalTime horarioSugerido,
        String observacao,
        List<PlanoAlimentarRefeicaoAlimentoResponse> alimentos
) {
}
