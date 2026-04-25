package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotBlank;

public record ExercicioRequest(
        @NotBlank(message = "Nome do exercicio e obrigatorio.")
        String nome,
        @NotBlank(message = "Grupo muscular e obrigatorio.")
        String grupoMuscular,
        String equipamento,
        String descricao,
        Boolean ativo
) {
}
