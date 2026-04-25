package br.com.evolucao.evolucaoFisica.dto;

import java.util.List;
import java.time.LocalDate;

public record PlanoAlimentarResponse(
        Long id,
        Long usuarioId,
        String nome,
        String descricao,
        Boolean ativo,
        Boolean publico,
        Boolean principal,
        LocalDate dataInicio,
        LocalDate dataFim,
        List<PlanoAlimentarDiaResponse> dias
) {
}
