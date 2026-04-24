package br.com.evolucao.evolucaoFisica.dto;

import java.util.List;

public record PlanoAlimentarResponse(
        Long id,
        Long usuarioId,
        String nome,
        String descricao,
        Boolean ativo,
        Boolean publico,
        List<PlanoAlimentarDiaResponse> dias
) {
}
