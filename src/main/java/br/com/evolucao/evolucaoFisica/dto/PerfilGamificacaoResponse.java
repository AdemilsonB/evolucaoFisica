package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TierRanking;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PerfilGamificacaoResponse(
        Long usuarioId,
        LocalDate dataInicio,
        BigDecimal pesoInicial,
        BigDecimal pesoAtual,
        Integer treinosRealizados,
        Integer sequenciaAtual,
        Integer melhorSequencia,
        Integer xpTotal,
        Integer nivelAtual,
        TierRanking tierAtual,
        Integer xpAtualNivel,
        Integer xpNecessarioProximoNivel,
        Integer percentualProgressoNivel
) {
}
