package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.VisibilidadeConteudo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrupoRequest(
        @NotBlank(message = "Nome do grupo e obrigatorio.")
        String nome,
        String descricao,
        @NotNull(message = "Criador e obrigatorio.")
        Long criadorId,
        @NotNull(message = "Visibilidade e obrigatoria.")
        VisibilidadeConteudo visibilidade
) {
}
