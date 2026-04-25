package br.com.evolucao.evolucaoFisica.dto;

public record ExercicioResponse(
        Long id,
        String nome,
        String grupoMuscular,
        String equipamento,
        String descricao,
        Boolean ativo
) {
}
