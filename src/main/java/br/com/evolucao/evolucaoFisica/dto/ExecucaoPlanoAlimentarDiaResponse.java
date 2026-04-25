package br.com.evolucao.evolucaoFisica.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExecucaoPlanoAlimentarDiaResponse(
        Long usuarioId,
        LocalDate dataReferencia,
        PlanoAlimentarResponse planoAlimentar,
        RegistroDiarioResponse registroDiario,
        List<PlanoAlimentarRefeicaoResponse> refeicoesPlanejadas,
        List<RefeicaoResponse> refeicoesExecutadas,
        BigDecimal proteinaConsumidaTotal,
        Integer quantidadeRefeicoesPlanejadas,
        Integer quantidadeRefeicoesExecutadas
) {
}
