package br.com.evolucao.evolucaoFisica.dto;

public record SeguidorResponse(
        Long id,
        Long seguidorId,
        String seguidorUsername,
        Long seguidoId,
        String seguidoUsername,
        Boolean ativo
) {
}
