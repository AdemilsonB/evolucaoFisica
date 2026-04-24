package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlanoAlimentarRequest(
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        @NotBlank(message = "Nome do plano alimentar e obrigatorio.")
        String nome,
        String descricao,
        Boolean ativo,
        Boolean publico
) {
}
